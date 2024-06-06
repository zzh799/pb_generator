package bean;

import com.intellij.protobuf.lang.psi.*;
import com.intellij.protobuf.lang.psi.util.PbCommentUtil;
import com.intellij.psi.PsiComment;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FieldBean {
    String name;
    int number;
    String type;
    String comment;

    boolean isRepeated;
    boolean isOptional;
    boolean isRequired;

    public FieldBean(PbField pbField) {
        this.name = pbField.getName();
        this.type = parseType(pbField.getTypeName());
        this.isRepeated = pbField.getCanonicalLabel() == PbField.CanonicalFieldLabel.REPEATED;
        // isOptional isRequired
        this.isOptional = pbField.getCanonicalLabel() == PbField.CanonicalFieldLabel.OPTIONAL;
        this.isRequired = pbField.getCanonicalLabel() == PbField.CanonicalFieldLabel.REQUIRED;

        this.number = pbField.getFieldNumber().getNumber().intValue();
        List<PsiComment> psiComments = PbCommentUtil.collectTrailingComments(pbField);
        if (psiComments.size() > 0) {
            this.comment = psiComments.get(0).getText().replace("//", "");
            if (this.comment.endsWith(" ")) {
                this.comment = this.comment.substring(0, this.comment.length() - 1);
            }
        } else {
            this.comment = "";
        }
    }

    protected String parseType(@Nullable PbTypeName typeName) {
        if (typeName.isBuiltInType()) {
            return typeName.getText();
        } else {
            return typeName.getText().replace(".", "_pb.");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean getIsRepeated() {
        return isRepeated;
    }

    public void setIsRepeated(boolean repeated) {
        isRepeated = repeated;
    }

    public boolean getIsOptional() {
        return isOptional;
    }

    public void setIsOptional(boolean optional) {
        isOptional = optional;
    }
    public boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(boolean required) {
        isRequired = required;
    }
    public String getDefaultValue(){
        if (type.equals("string")) {
            return "\"\"";
        } else if (type.equals("bool")) {
            return "false";
        } else if (type.equals("int32") || type.equals("int64") || type.equals("uint32") || type.equals("uint64")) {
            return "0";
        } else if (type.equals("float") || type.equals("double")) {
            return "0.0";
        } else {
            return "null";
        }
    }
}
