package bean;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.openapi.components.Storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "PbGeneratorSettings", storages = @Storage("pb_generator.xml"))
public class ProtobufGeneratorSettings implements PersistentStateComponent<ProtobufGeneratorSettings> {
    private String author = "Unknown";
    private boolean generateFromFileName = true;
//    private String protoResDir = "proto";
    private String protoGenDesDir = "gen";
//    private String UIGenDesDir = "Assets/LuaFramework/Lua/UI";
    private String tempDir = "pb_template";

    public static ProtobufGeneratorSettings getInstance() {
        return ApplicationManager.getApplication().getService(ProtobufGeneratorSettings.class);
    }

    @Override
    public @Nullable
    ProtobufGeneratorSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ProtobufGeneratorSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isGenerateFromFileName() {
        return generateFromFileName;
    }

    public void setGenerateFromFileName(boolean generateFromFileName) {
        this.generateFromFileName = generateFromFileName;
    }

//    public String getProtoResDir() {
//        return protoResDir;
//    }
//
//    public void setProtoResDir(String protoResDir) {
//        this.protoResDir = protoResDir;
//    }

    public String getProtoGenDesDir() {
        return protoGenDesDir;
    }

    public void setProtoGenDesDir(String protoGenDesDir) {
        this.protoGenDesDir = protoGenDesDir;
    }

//    public String getUIGenDesDir() {
//        return UIGenDesDir;
//    }
//
//    public void setUIGenDesDir(String UIGenDesDir) {
//        this.UIGenDesDir = UIGenDesDir;
//    }

    public String getTempDir() {
        return tempDir;
    }

    public void setTempDir(String tempDir) {
        this.tempDir = tempDir;
    }
}
