import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;


/**
 * @ClassName StringConvert.java
 * @Author guanliqun
 * @Description 文本转换
 * @CreateTime 2021/10/27
 */
public class StringConvert extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        SelectionModel selectionModel = editor.getSelectionModel();
        String selectText = selectionModel.getSelectedText();
        if(StrUtil.isBlankIfStr(selectText)){
            return;
        }
        //替换选择的报文部分
        final Document document = editor.getDocument();
        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
        int start = primaryCaret.getSelectionStart();
        int end = primaryCaret.getSelectionEnd();
        //获取当前在操作的工程上下文
        Project project = e.getData(PlatformDataKeys.PROJECT);
        WriteCommandAction.runWriteCommandAction(project, () ->
                document.replaceString(start, end, convert(selectText))
        );
        // 取消选择范围
//        primaryCaret.removeSelection();
    }

    /***
     * someThing->some_thing->SOME_THING->someThing
     * @param str 待宰羊肉
     * @return 现羊肉
     */
    public static String convert(String str) {
        if (str.contains("_")) {
            if (ReUtil.contains("[a-z]+", str)) {
                return str.toUpperCase();
            }
            return NamingCase.toCamelCase(str);
        }
        if(ReUtil.isMatch("[a-z]+",str)){
            return str.toUpperCase();
        }
        if(ReUtil.isMatch("[A-Z]+",str)){
            return str.toLowerCase();
        }
        return NamingCase.toUnderlineCase(str);
    }

    public static void main(String[] args) {
       String s=convert("ASDF");
       System.out.println(s);
    }

}
