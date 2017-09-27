import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiStatement;

/**
 * Generates a static nested Builder class.
 */
public class BuilderClassGenerator implements BuilderGenerator {

    @Override
    public void generate(PsiClass psiClass, GenerateBuilderDialog dialog) {
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiClass.getProject());
        PsiClass builderClass = elementFactory.createClass("Builder");

        for (PsiField field : dialog.getFields()) {
            builderClass.add(createSetter(elementFactory, builderClass, psiClass, field));
        }

        builderClass.add(createBuildMethod(elementFactory, builderClass, psiClass));

        psiClass.add(createBuilderMethod(elementFactory, builderClass, psiClass));

        psiClass.add(builderClass);
    }

    private PsiMethod createSetter(PsiElementFactory elementFactory, PsiClass builderClass, PsiClass psiClass, PsiField field) {
        PsiMethod builderMethod = elementFactory.createMethod(
                field.getName(),
                elementFactory.createType(builderClass));
        PsiParameter parameter = elementFactory.createParameter(field.getName(), field.getType());
        builderMethod.getParameterList().add(parameter);

        String assignBuilder = psiClass.getName() + ".this." + field.getName() + " = " + field.getName() +
                ";\n";

        PsiStatement assignStatement = elementFactory.createStatementFromText(
                assignBuilder,
                builderClass);

        PsiStatement returnStatement = elementFactory.createStatementFromText(
                "return this;\n",
                builderClass);

        builderMethod.getBody().add(assignStatement);
        builderMethod.getBody().add(returnStatement);
        return builderMethod;
    }

    private PsiMethod createBuildMethod(PsiElementFactory elementFactory, PsiClass builderClass, PsiClass psiClass) {
        PsiMethod buildMethod = elementFactory.createMethod(
                "build",
                elementFactory.createType(psiClass));

        PsiStatement returnStatement = elementFactory.createStatementFromText(
                "return " + psiClass.getName() + ".this;\n",
                builderClass);

        buildMethod.getBody().add(returnStatement);
        return buildMethod;
    }

    private PsiMethod createBuilderMethod(PsiElementFactory elementFactory, PsiClass builderClass, PsiClass psiClass) {
        PsiMethod builderMethod = elementFactory.createMethod("builder",
                elementFactory.createType(builderClass));

        builderMethod.getModifierList().add(elementFactory.createKeyword("static"));

        PsiStatement builderStatement = elementFactory.createStatementFromText(
                "return new " + psiClass.getName() + "().new Builder();\n",
                psiClass
        );

        builderMethod.getBody().add(builderStatement);
        return builderMethod;
    }

    @Override
    public void rollback(PsiClass psiClass, GenerateBuilderDialog dialog) {
        PsiClass[] innerClasses = psiClass.getInnerClasses();
        for (PsiClass innerClass : innerClasses) {
            if (innerClass.getName().equals("Builder")) {
                innerClass.delete();
                break;
            }
        }
    }
}
