package bean;

import com.intellij.protobuf.lang.psi.PbEnumValue;
import com.intellij.protobuf.lang.psi.PbNumberValue;
import com.intellij.protobuf.lang.psi.util.PbCommentUtil;
import com.intellij.psi.PsiComment;

import java.util.List;

public class EnumValueBean {
    private final String name;
    private final int value;
    private String comment;

    public EnumValueBean(PbEnumValue pbEnumValue) {
        name = pbEnumValue.getName();
        value = pbEnumValue.getNumberValue().getNumber().intValue();
        List<PsiComment> psiComments = PbCommentUtil.collectTrailingComments(pbEnumValue);
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

    public int getValue() {
        return value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
