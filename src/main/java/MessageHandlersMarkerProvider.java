import com.intellij.codeInsight.daemon.*;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MessageHandlersMarkerProvider extends RelatedItemLineMarkerProvider {

    private final static String COMMAND_INTERFACE = "\\Damejidlo\\CommandBus\\ICommand";
    private final static String EVENT_INTERFACE = "\\Damejidlo\\EventBus\\IDomainEvent";
    private final static String COMMAND_HANDLER_INTERFACE = "\\Damejidlo\\CommandBus\\ICommandHandler";
    private final static String EVENT_SUBSCRIBER_INTERFACE = "\\Damejidlo\\EventBus\\IEventSubscriber";

    private final static Logger LOG = Logger.getInstance(MessageHandlersMarkerProvider.class);

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo> result) {
        final boolean elementIsNewExpression;
        elementIsNewExpression = element instanceof NewExpression;
        if (!elementIsNewExpression) {
            return;
        }

        final NewExpression newExpressionElement = (NewExpression) element;

        // optimization
        final String className = newExpressionElement.getType().toString();
        if (!className.endsWith("Command") && !className.endsWith("Event")) {
            return;
        }

        final PhpClass elementClass = getPhpClass(newExpressionElement.getType(), newExpressionElement.getProject());
        if (elementClass == null) {
            return;
        }

        if (!doesClassImplementInterface(elementClass, COMMAND_INTERFACE) && !doesClassImplementInterface(elementClass, EVENT_INTERFACE)) {
            return;
        }

        final List<PhpClass> commandHandlers = getHandlerClasses(elementClass, COMMAND_HANDLER_INTERFACE);
        final List<PhpClass> eventSubscribers = getHandlerClasses(elementClass, EVENT_SUBSCRIBER_INTERFACE);

        final List<PhpClass> targets = new ArrayList<>();
        targets.addAll(commandHandlers);
        targets.addAll(eventSubscribers);

        if (targets.isEmpty()) {
            return;
        }

        NavigationGutterIconBuilder<PsiElement> builder =
                NavigationGutterIconBuilder.create(Icons.EVENT_ICON).
                        setTargets(targets).
                        setTooltipText("Attached handlers or subscribers");
        result.add(builder.createLineMarkerInfo(newExpressionElement.getFirstChild()));
    }

    @Nullable
    private PhpClass getPhpClass(@NotNull PhpType phpType,@NotNull Project project) {
        PhpIndex phpIndex = PhpIndex.getInstance(project);
        Collection<PhpClass> classes = phpIndex.getClassesByFQN(phpType.toString());

        if (classes.isEmpty()) {
            return null;
        }

        return classes.iterator().next();
    }

    private boolean doesClassImplementInterface(@NotNull PhpClass phpClass, @NotNull String interfaceName) {
        if (doesClassImplementInterfaceRecursive(phpClass, interfaceName)) {
            return true;
        }

        final PhpClass superClass = phpClass.getSuperClass();

        if (superClass == null) {
            return false;
        } else {
            return doesClassImplementInterface(superClass, interfaceName);
        }
    }

    private boolean doesClassImplementInterfaceRecursive(@NotNull PhpClass phpClass, @NotNull String interfaceName) {
        final PhpClass[] implementedInterfaces = phpClass.getImplementedInterfaces();

        for (PhpClass implementedInterface : implementedInterfaces) {
            if (implementedInterface.getFQN().equals(interfaceName) || doesClassImplementInterfaceRecursive(implementedInterface, interfaceName)) {
                return true;
            }

            if (doesClassImplementInterfaceRecursive(implementedInterface, interfaceName)) {
                return true;
            }
        }

        return false;
    }

    @NotNull
    private List<PhpClass> getHandlerClasses(@NotNull PhpClass messageClass, @NotNull String implementingInterface) {
        final List<PhpClass> targets = new ArrayList<>();

        final PhpIndex phpIndex = PhpIndex.getInstance(messageClass.getProject());
        for (PhpClass handlerClass : phpIndex.getAllSubclasses(implementingInterface)) {
            final Method handleMethod = handlerClass.findMethodByName("handle");
            if (handleMethod == null) {
                continue;
            }

            final Parameter[] parameters = handleMethod.getParameters();

            if (parameters.length != 1) {
                continue;
            }

            final Parameter parameter = parameters[0];

            final PhpClass parameterClass = getPhpClass(parameter.getType(), messageClass.getProject());

            if (parameterClass == null) {
                continue;
            }

            if (parameterClass.equals(messageClass)) {
                targets.add(handlerClass);
            }
        }

        return targets;
    }

}
