package bean;

import com.intellij.protobuf.lang.psi.*;
import com.intellij.protobuf.lang.psi.impl.PbMessageBodyImpl;
import com.intellij.psi.PsiElement;
import groovyjarjarantlr.debug.MessageAdapter;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageBean {
    String packageName;
    String name;
    List<FieldBean> fields;
    List<MapFieldBean> mapFields;
    List<MessageBean> subMessages;
    List<EnumBean> enums;
    public MessageBean(PbMessageDefinition pbMessageDefinition) {
        PbPackageStatement packageStatement = pbMessageDefinition.getPbFile().getPackageStatement();
        String fileName = "";
        if (packageStatement == null || packageStatement.getPackageName() == null) {
            fileName = pbMessageDefinition.getPbFile().getName().replace(".proto", "");
            this.packageName = fileName;
        } else {
            this.packageName = packageStatement.getPackageName().getText();
        }
        PsiElement nameIdentifier = pbMessageDefinition.getNameIdentifier();
        if (nameIdentifier == null) {
            this.name = "";
        } else {
            this.name = nameIdentifier.getText();
        }
        this.fields = new ArrayList<>();
        this.mapFields = new ArrayList<>();
        this.subMessages = new ArrayList<>();
        this.enums = new ArrayList<>();
        PbMessageBody body = pbMessageDefinition.getBody();
        if (body != null) {
            body.getSimpleFieldList().forEach(pbSimpleField -> fields.add(new FieldBean(pbSimpleField)));
            body.getMapFieldList().forEach(mapField -> mapFields.add(new MapFieldBean(mapField)));
            body.getMessageDefinitionList().forEach(message -> subMessages.add(new MessageBean(message)));
            body.getEnumDefinitionList().forEach(enumDefinition -> enums.add(new EnumBean(enumDefinition)));
        }
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FieldBean> getFields() {
        return fields;
    }

    public void setFields(List<FieldBean> fields) {
        this.fields = fields;
    }

    public List<MapFieldBean> getMapFields() {
        return mapFields;
    }

    public void setMapFields(List<MapFieldBean> mapFields) {
        this.mapFields = mapFields;
    }

    public List<MessageBean> getSubMessages() {
        return subMessages;
    }

    public void setSubMessages(List<MessageBean> subMessages) {
        this.subMessages = subMessages;
    }


    public List<EnumBean> getEnums() {
        return enums;
    }

    public void setEnums(List<EnumBean> enums) {
        this.enums = enums;
    }

}
