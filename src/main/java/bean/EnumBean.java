package bean;

import com.intellij.protobuf.lang.psi.*;
import com.intellij.protobuf.lang.psi.impl.PbEnumDefinitionImpl;
import com.intellij.psi.PsiElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnumBean {
    String packageName;
    String name;
    List<EnumValueBean> values;

    public EnumBean(PbEnumDefinition psiElement) {
        PbPackageStatement packageStatement = psiElement.getPbFile().getPackageStatement();
        String fileName = "";
        if (packageStatement == null || packageStatement.getPackageName() == null) {
            fileName = psiElement.getPbFile().getName().replace(".proto", "");
            this.packageName = fileName;
        } else {
            this.packageName = packageStatement.getPackageName().getText();
        }
        PsiElement nameIdentifier = psiElement.getNameIdentifier();
        if (nameIdentifier == null) {
            this.name = "";
        } else {
            this.name = nameIdentifier.getText();
        }

        this.values = new ArrayList<>();
        PbEnumBody body = psiElement.getBody();
        if (body != null) {
            for (PbEnumValue pbEnumValue : body.getEnumValueList()) {
                values.add(new EnumValueBean(pbEnumValue));
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


    public List<EnumValueBean> getValues() {
        return values;
    }

    public void setValues(List<EnumValueBean> values) {
        this.values = values;
    }
}
