package marker;

import bean.ProtobufGeneratorSettings;
import com.intellij.codeInsight.daemon.*;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.IconLoader;
import com.intellij.protobuf.lang.psi.impl.PbMessageDefinitionImpl;
import com.intellij.psi.PsiElement;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;
import generator.ProtoGenerator;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class MessageLineMarkerProvider implements LineMarkerProvider {
    private static final Icon ICON = IconLoader.getIcon("/icons/protoMessage.png");


    private static final GutterIconNavigationHandler<PbMessageDefinitionImpl> SHOW_SENDERS =
            (e, psiElement) -> {
                if (psiElement != null) {
                    Project project = psiElement.getProject();
                    String projectPathUri = project.getBasePath();
                    if (projectPathUri == null) {
                        Messages.showErrorDialog("please open project first", "Error");
                        return;
                    }

                    Path projectPath = Path.of(projectPathUri);
                    ProtobufGeneratorSettings settings = ProtobufGeneratorSettings.getInstance();
                    List<File> files = List.of(Objects.requireNonNull(projectPath.resolve(settings.getTempDir()).toFile().listFiles(pathname -> !pathname.isDirectory())));
                    if (files.size() == 0) {
                        Messages.showErrorDialog("no template file found", "Error");
                        return;
                    }

                    JBPopupFactory instance = JBPopupFactory.getInstance();// 创建实例
                    // 添加监听事件
                    instance.createPopupChooserBuilder(files)
                            .setTitle("选择模板生成[" + psiElement.getName() + "]")
                            .setRenderer((list, value, index, isSelected, cellHasFocus) -> {
                                JLabel label = new JLabel();
                                label.setText(value.getName());
                                return label;
                            })
                            .setItemChosenCallback(file -> ProtoGenerator.generate(file, psiElement, e))
                            .createPopup()
                            .show(RelativePoint.fromScreen(e.getLocationOnScreen()));
                }
            };


    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement psiElement) {
        if (psiElement instanceof PbMessageDefinitionImpl) { // ACTUALLY DONT!
            PbMessageDefinitionImpl element = (PbMessageDefinitionImpl) psiElement;
            return new LineMarkerInfo<>(element,
                    element.getTextRange(),
                    ICON,
                    e -> "generate " + element.getName(),
                    SHOW_SENDERS,
                    GutterIconRenderer.Alignment.LEFT,
                    () -> null);
        } else {
            return null;
        }
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<? extends PsiElement> elements, @NotNull Collection<? super LineMarkerInfo<?>> result) {
        for (PsiElement psiElement : elements) {
            if (psiElement instanceof PbMessageDefinitionImpl) { // ACTUALLY DONT!
                PbMessageDefinitionImpl element = (PbMessageDefinitionImpl) psiElement;
                result.add(new LineMarkerInfo<>(element,
                        element.getTextRange(),
                        ICON,
                        e -> "generate " + element.getName(),
                        SHOW_SENDERS,
                        GutterIconRenderer.Alignment.LEFT,
                        () -> null));
            }
        }
//        LineMarkerProvider.super.collectSlowLineMarkers(elements, result);
    }
}
