package generator;

import bean.EnumBean;
import bean.MessageBean;
import bean.ProtobufGeneratorSettings;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.protobuf.lang.psi.impl.PbEnumDefinitionImpl;
import com.intellij.protobuf.lang.psi.impl.PbMessageDefinitionImpl;
import utils.FileUtils;
import utils.StringUtils;
import utils.VelocityUtils;

import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProtoGenerator {
    public static void generate(File template,Project project,HashMap<String, Object> map ,MouseEvent e){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        map.put("date", dateFormat.format(new Date()));
        map.put("author", ProtobufGeneratorSettings.getInstance().getAuthor());
        map.put("StringUtils", new StringUtils());

        try {
            ProtobufGeneratorSettings settings = ProtobufGeneratorSettings.getInstance();
            Path basePath = Path.of(Objects.requireNonNull(project.getBasePath()));
            Path outputDirPath = basePath.resolve(settings.getProtoGenDesDir());
            outputDirPath = FileUtils.getPath(outputDirPath, map.get("packageName").toString(), settings.isGenerateFromFileName());
            String ext = template.getPath().substring(template.getPath().lastIndexOf('.'));
            String fileName = map.get("messageName").toString() + ext;
            File outputFile = outputDirPath.resolve(fileName).toFile();
            FileUtils.showGeneratePop(VelocityUtils.generate(FileUtils.ReadAll(template), map), e, project, outputFile);
        } catch (IOException err) {
            Messages.showErrorDialog("gen failed", "Error");
        }
    }

    public static void generate(File template, PbMessageDefinitionImpl pbMessageDefinition, MouseEvent e) {
        Project project = pbMessageDefinition.getProject();

        HashMap<String, Object> map = new HashMap<>();
        MessageBean value = new MessageBean(pbMessageDefinition);
        map.put("packageName", value.getPackageName());
        map.put("messageName", value.getName());
        map.put("message", value);

        generate(template,project, map,e);


    }

    public static void generate(File template, PbEnumDefinitionImpl psiElement, MouseEvent e) {
        Project project = psiElement.getProject();

        HashMap<String, Object> map = new HashMap<>();
        EnumBean value = new EnumBean(psiElement);
        map.put("packageName", value.getPackageName());
        map.put("messageName", value.getName());
        map.put("enum", value);

        generate(template,project, map,e);
    }
}
