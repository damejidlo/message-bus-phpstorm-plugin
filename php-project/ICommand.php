<?php
declare(strict_types=1);

namespace Damejidlo\CommandBus;

use Damejidlo\MessageBus\IBusMessage;



/**
 * Command implementation must adhere to these rules:
 * - class must be named <command-name>Command
 * - class must be final
 * - command name should be in imperative form ("do something")
 * - command must be a simple immutable DTO
 * - command must not contain entities, only references (i.e. "int $orderId", not "Order $order")
 * - class must be annotated with reference to handler for easy navigation in IDE like this:
 * "@see SpecificHandler"
 *
 * Examples of good command class names:
 * - RejectOrderCommand
 * - CreateUserCommand
 */
interface ICommand extends IBusMessage
{

}
