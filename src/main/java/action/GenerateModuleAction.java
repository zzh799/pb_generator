package action;

import bean.ProtobufGeneratorSettings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.protobuf.lang.PbFileType;
import org.jetbrains.annotations.NotNull;
import utils.FileUtils;
import utils.NotifyUtils;
import utils.VelocityUtils;

import java.io.*;
import java.nio.file.Path;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GenerateModuleAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ProtobufGeneratorSettings settings = ProtobufGeneratorSettings.getInstance();
        Project project = e.getProject();
        assert project != null;
        String basePath = project.getBasePath();
        assert basePath != null;
        Path projectBasePath = Path.of(basePath);
        VirtualFile vFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        String packageName = vFile != null ? vFile.getName().replace(".proto", "") : "";
        Path outputDirPath = projectBasePath.resolve(settings.getProtoGenDesDir());
        outputDirPath = FileUtils.getPath(outputDirPath, packageName, settings.isGenerateFromFileName());
        if (!outputDirPath.toFile().exists() && !outputDirPath.toFile().mkdirs()) {
            Messages.showErrorDialog("create dir failed!", "Error");
            return;
        }
        String moduleName = outputDirPath.getFileName().toString();

        File[] templates = projectBasePath.resolve(settings.getTempDir()).toFile().listFiles(pathname -> !pathname.isDirectory());

        Map<String, Object> map = new HashMap<>();
        map.put("moduleName", moduleName);
        map.put("author", ProtobufGeneratorSettings.getInstance().getAuthor());
        map.put("dataTime", DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA).format(new Date()));


        assert templates != null;
        for (File template : templates) {

            String newFileName = template.getName().replace("${moduleName}", moduleName);
            File newFile = outputDirPath.resolve(newFileName).toFile();
            if (!newFile.exists()) {
                try {
                    FileUtils.WriteAll(newFile, VelocityUtils.generate(FileUtils.ReadAll(template), map));
                    FileUtils.OpenFile(project, newFile);
                    NotifyUtils.notifyInfo(project, newFile.getName() + "gen success");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    NotifyUtils.notifyError(project, newFile.getName() + "gen failed");
                }
            } else {
                NotifyUtils.notifyError(project, newFile.getName() + "already exist");
            }
        }
        NotifyUtils.notifyInfo(project, moduleName + "module gen complete");
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        VirtualFile vFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        e.getPresentation().setEnabledAndVisible(vFile != null && vFile.getFileType() instanceof PbFileType);
    }
}
