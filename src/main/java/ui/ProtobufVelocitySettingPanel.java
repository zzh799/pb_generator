package ui;

import bean.ProtobufGeneratorSettings;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ProtobufVelocitySettingPanel implements SearchableConfigurable, Configurable.NoScroll {
    private final ProtobufGeneratorSettings settings;
    private JTextField textField_author;
    private JCheckBox checkBox_GenerateFromFileName;
    private JTextField textField_ProtoResDir;
    private JTextField textField_ProtoGenDesDir;
    private JTextField textField_UIGenDesDir;
    private JTextField textField_TempDir;
    private JPanel myPanel;

    public ProtobufVelocitySettingPanel() {
        this.settings = ProtobufGeneratorSettings.getInstance();
        textField_author.setText(String.valueOf(settings.getAuthor()));
        textField_ProtoResDir.setText(String.valueOf(settings.getProtoResDir()));
        textField_ProtoGenDesDir.setText(String.valueOf(settings.getProtoGenDesDir()));
        textField_UIGenDesDir.setText(String.valueOf(settings.getUIGenDesDir()));
        textField_TempDir.setText(String.valueOf(settings.getTempDir()));
        checkBox_GenerateFromFileName.setSelected(settings.isGenerateFromFileName());
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    @Override
    public @NotNull
    @NonNls
    String getId() {
        return "Lua Generator";
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Lua Generator";
    }

    @Override
    public @Nullable
    JComponent createComponent() {
        return myPanel;
    }

    @Override
    public boolean isModified() {
        return !StringUtil.equals(settings.getAuthor(), textField_author.getText()) ||
                !StringUtil.equals(settings.getProtoResDir(), textField_ProtoResDir.getText()) ||
                !StringUtil.equals(settings.getProtoGenDesDir(), textField_ProtoGenDesDir.getText()) ||
                !StringUtil.equals(settings.getUIGenDesDir(), textField_UIGenDesDir.getText()) ||
                !StringUtil.equals(settings.getTempDir(), textField_TempDir.getText()) ||
                settings.isGenerateFromFileName() != checkBox_GenerateFromFileName.isSelected();
    }

    @Override
    public void apply() throws ConfigurationException {
        settings.setAuthor(textField_author.getText());
        settings.setProtoResDir(textField_ProtoResDir.getText());
        settings.setProtoGenDesDir(textField_ProtoGenDesDir.getText());
        settings.setUIGenDesDir(textField_UIGenDesDir.getText());
        settings.setTempDir(textField_TempDir.getText());
        settings.setGenerateFromFileName(checkBox_GenerateFromFileName.isSelected());
    }
}
