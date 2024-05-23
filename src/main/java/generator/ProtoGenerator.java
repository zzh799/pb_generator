package generator;

import bean.MessageBean;
import bean.ProtobufGeneratorSettings;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.protobuf.lang.psi.impl.PbMessageDefinitionImpl;
import utils.FileUtils;
import utils.StringUtils;
import utils.VelocityUtils;

import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Path;
import java.text.DateFormat;
import java.util.*;

public class ProtoGenerator {
    public static void generate(File template, PbMessageDefinitionImpl pbMessageDefinition, MouseEvent e) {
        Project project = pbMessageDefinition.getProject();

        HashMap<String, Object> map = new HashMap<>();
        map.put("date", DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA).format(new Date()));
        map.put("author", ProtobufGeneratorSettings.getInstance().getAuthor());
        map.put("StringUtils", new StringUtils());
        map.put("messageDefinition", pbMessageDefinition);
        MessageBean value = new MessageBean(pbMessageDefinition);
        map.put("message", value);
        map.put("packageName", value.getPackageName());
        map.put("messageName", value.getName());
        try {
            ProtobufGeneratorSettings settings = ProtobufGeneratorSettings.getInstance();
            Path basePath = Path.of(Objects.requireNonNull(project.getBasePath()));
            Path outputDirPath = basePath.resolve(settings.getProtoGenDesDir());
            outputDirPath = FileUtils.getPath(outputDirPath, value.getPackageName(), settings.isGenerateFromFileName());
            String ext = template.getPath().substring(template.getPath().lastIndexOf('.'));
            String fileName = value.getName().replace("Msg", "") + ext;
            File outputFile = outputDirPath.resolve(fileName).toFile();
            FileUtils.showGeneratePop(VelocityUtils.generate(FileUtils.ReadAll(template), map), e, project, outputFile);
        } catch (IOException err) {
            Messages.showErrorDialog("gen failed", "Error");
        }
    }
}
