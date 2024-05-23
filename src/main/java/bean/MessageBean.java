package bean;

import com.intellij.protobuf.lang.psi.*;
import com.intellij.protobuf.lang.psi.impl.PbMessageBodyImpl;
import com.intellij.psi.PsiElement;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageBean {
    String packageName;
    String name;
    List<FieldBean> fields;

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

        PbMessageBody body = pbMessageDefinition.getBody();
        if (body != null) {
            for (PbSimpleField pbSimpleField : body.getSimpleFieldList()) {
                fields.add(new FieldBean(pbSimpleField));
            }
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
}
