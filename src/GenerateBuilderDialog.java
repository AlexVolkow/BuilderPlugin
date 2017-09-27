import com.google.common.collect.Lists;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.*;

/**
 * A {@link DialogWrapper} for generating a Builder class.
 */
public class GenerateBuilderDialog extends DialogWrapper {

    private final LabeledComponent<JPanel> component;
    private final List<PsiField> fields;

    public GenerateBuilderDialog(PsiClass psiClass) {
        super(psiClass.getProject());
        setTitle("Select Fields for Builder");

        fields = getFields(psiClass);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createTableWithToolbar());

        component = LabeledComponent.create(panel, "Fields to include in Builder:");
        init();
    }

    public List<PsiField> getFields() {
        return fields;
    }

    @Override
    protected JComponent createCenterPanel() {
        return component;
    }

    private JPanel createTableWithToolbar() {
        JBList<String> list = new JBList<>(fields.stream().map(PsiField::getName).toArray());
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(list);
        decorator.disableAddAction();
        return decorator.createPanel();
    }

    private List<PsiField> getFields(PsiClass psiClass) {
        List<PsiField> memberFields = Lists.newArrayList();
        for (PsiField field : psiClass.getFields()) {
            if (field.hasModifierProperty(PsiModifier.STATIC)) {
                continue;
            }
            memberFields.add(field);
        }
        return memberFields;
    }
}
