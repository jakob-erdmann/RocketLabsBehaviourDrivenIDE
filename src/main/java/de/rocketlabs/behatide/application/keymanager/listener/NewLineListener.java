package de.rocketlabs.behatide.application.keymanager.listener;

import com.google.common.base.Strings;
import de.rocketlabs.behatide.application.component.Editor;
import de.rocketlabs.behatide.application.keymanager.KeyEventListener;
import javafx.scene.input.KeyEvent;
import org.jetbrains.annotations.NotNull;

public class NewLineListener implements KeyEventListener {

    private static final char NEW_LINE_CHARACTER = '\n';

    private Editor editor;

    public NewLineListener(@NotNull Editor editor) {
        this.editor = editor;
    }

    private int countLeadingOccurrences(String haystack, char needle) {
        int count = 0;
        while (count < haystack.length() && haystack.charAt(count) == needle) {
            count++;
        }
        return count;
    }

    @Override
    public void handleEvent(KeyEvent event) {
        String str = editor.getText().substring(0, editor.getCaretPosition());
        int lastLineBreak;
        if (editor.getLineIndex() > 0) {
            lastLineBreak = Math.min(str.lastIndexOf(NEW_LINE_CHARACTER) + 1, str.length());
        } else {
            lastLineBreak = 0;
        }
        String lastLine = str.substring(lastLineBreak);
        Integer nextIndent = countLeadingOccurrences(lastLine, ' ');
        String s = NEW_LINE_CHARACTER + Strings.repeat(" ", Math.max(0, nextIndent));
        editor.insertText(editor.getCaretPosition(), s);

        event.consume();
    }
}
