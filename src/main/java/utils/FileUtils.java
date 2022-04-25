package utils;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffContentFactoryImpl;
import com.intellij.diff.DiffManager;
import com.intellij.diff.contents.DiffContent;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.ThrowableRunnable;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static void showGeneratePop(String result, MouseEvent e, Project project, File outputFile) {
        File outputDir = outputFile.getParentFile();
        JBPopupFactory instance = JBPopupFactory.getInstance();// 创建实例
        List<String> list = new ArrayList<>();

        if (outputFile.exists()) {
            list.add("compare" + outputFile.getPath());
        } else {
            list.add("gen file:" + outputFile.getPath());
        }
        list.add("save to...");
        list.add("copy to clipboard");

        instance.createPopupChooserBuilder(list)
                .setTitle("choose operation")// 设置标题
                .setItemChosenCallback(s -> {
                    if (s.contains("gen file")) {
                        if (!outputDir.exists()) {
                            if (!outputDir.mkdirs()) {
                                Messages.showErrorDialog("create dir failed", "error");
                                return;
                            }
                        }
                        if (outputFile.exists()) {
                            OpenFile(project, outputFile);//打开文件
                            Messages.showErrorDialog("file already exist", "error");
                        } else {
                            try {
                                if (outputFile.createNewFile()) {
                                    WriteAll(outputFile, result);
                                    OpenFile(project, outputFile);
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else if (s.contains("compare")) {
                        comapreFileWithText(project, outputFile, result);
                    } else if (s.equals("copy to clipboard")) {
                        copyToClipboard(result);
                    } else if (s.equals("save to...")) {
                        try {
                            openSaveFileDialog(project, outputFile.getName(), result);
                        } catch (Throwable ex) {
                            ex.printStackTrace();
                        }
                    }
                })
                .createPopup()
                .show(RelativePoint.fromScreen(e.getLocationOnScreen()));
    }

    public static void WriteAll(File outputFile, String result) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputFile);
        fos.write(result.getBytes(StandardCharsets.UTF_8));
        fos.close();
    }

    public static void copyToClipboard(String result) {
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(result);
        sysClip.setContents(tText, null);
    }

    public static void comapreFileWithText(Project project, File outputFile, String text) {
        VirtualFile outputVirtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(outputFile.toString());
        DiffContentFactory diffContentFactory = new DiffContentFactoryImpl();
        DiffContent diffContent1 = diffContentFactory.createEditable(project, text, outputVirtualFile.getFileType());
        assert outputVirtualFile != null;
        DiffContent diffContent2 = diffContentFactory.create(project, outputVirtualFile);
        SimpleDiffRequest simpleDiffRequest = new SimpleDiffRequest(outputFile.getName(), diffContent1, diffContent2, "生成内容", "当前内容");
        DiffManager.getInstance().showDiff(project, simpleDiffRequest);
    }

    public static void OpenFile(Project project, File outputFile) {
        VirtualFile outputVirtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(outputFile.toString());
        if (outputVirtualFile != null) {
            new OpenFileDescriptor(project, outputVirtualFile).navigate(true);
        }
    }

    public static void openSaveFileDialog(Project project, String fileName, String result) throws Throwable {
        FileSaverDescriptor fileSaverDescriptor = new FileSaverDescriptor("保存" + fileName, "保存" + fileName, "lua");
        String basePath = project.getBasePath();
        assert basePath != null;
        VirtualFileWrapper wrapper = FileChooserFactory
                .getInstance()
                .createSaveFileDialog(fileSaverDescriptor, project)
                .save(Path.of(basePath), fileName);

        if (wrapper == null) return;
        VirtualFile file = wrapper.getVirtualFile(true);

        if (file == null) throw new Exception("create file failed");

        WriteCommandAction.writeCommandAction(project).run((ThrowableRunnable<Throwable>) () -> {
            VfsUtil.saveText(file, result);
            new OpenFileDescriptor(project, file).navigate(true);
        });

    }

    public static Path getPath(Path outputDirPath, String packageName, boolean isGenerateFromFileName) {
        if (isGenerateFromFileName) {
            String[] paths = packageName.split("_");
            for (String path : paths) {
                outputDirPath = outputDirPath.resolve(StringUtils.upperFirstCase(path));
            }
        } else {
            outputDirPath = outputDirPath.resolve(StringUtils.upperFirstCase(packageName));
        }
        return outputDirPath;
    }

    public static String ReadAll(File template) throws IOException {
        FileInputStream fio = new FileInputStream(template);
        return new String(fio.readAllBytes(), StandardCharsets.UTF_8);
    }
}
