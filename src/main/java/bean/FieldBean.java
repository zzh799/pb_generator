package bean;

import com.intellij.protobuf.lang.psi.*;
import com.intellij.protobuf.lang.psi.util.PbCommentUtil;
import com.intellij.psi.PsiComment;

import java.util.List;

public class FieldBean {
    String name;
    int number;
    String type;
    String comment;

    public FieldBean(PbSimpleField pbSimpleField) {
        this.name = pbSimpleField.getName();
        PbTypeName typeName = pbSimpleField.getTypeName();
        if (typeName.isBuiltInType()) {
            this.type = typeName.getText();
        } else {
            this.type = typeName.getText().replace(".", "_pb.");
        }

        this.number = pbSimpleField.getFieldNumber().getNumber().intValue();
        List<PsiComment> psiComments = PbCommentUtil.collectTrailingComments(pbSimpleField);
        if (psiComments.size() > 0) {
            this.comment = psiComments.get(0).getText().replace("//", "");
            if (this.comment.endsWith(" ")) {
                this.comment = this.comment.substring(0, this.comment.length() - 1);
            }
        } else {
            this.comment = "";
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
}
