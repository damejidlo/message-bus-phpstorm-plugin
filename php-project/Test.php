<?php declare(strict_types = 1);

namespace Damejidlo;

use Damejidlo\CommandBus\ICommand;
use Damejidlo\CommandBus\ICommandHandler;
use Damejidlo\EventBus\IDomainEvent;
use Damejidlo\EventBus\IEventSubscriber;
use Damejidlo\Orders\Receipt\CreateOrderReceiptCommand;



class Test
{

	public function createCommand(): void
	{
		$command = new DoSomethingCommand();

		// no handlers
		$command = new DoSomethingCommand2();

		// no handlers
		$foo = new \stdClass();

		// two subscribers
		$event = new SomethingHappenedEvent();

		$command = new CreateOrderReceiptCommand(1);
	}

}



final class DoSomethingCommand extends Message
{

}



final class DoSomethingHandler implements ICommandHandler
{

	public function handle(DoSomethingCommand $command) : void
	{

	}

}



class DoSomethingCommand2 extends Message
{

}



class SomethingHappenedEvent extends Message implements IDomainEvent
{

}



class DoSomethingOnSomethingHappened implements IEventSubscriber
{

	public function handle(SomethingHappenedEvent $event) : void
	{

	}

}



class DoSomethingElseOnSomethingHappened implements IEventSubscriber
{

	public function handle(SomethingHappenedEvent $event) : void
	{

	}

}



class Message implements ICommand
{

	/**
	 * @return mixed[]
	 */
	public function toArray(): array
	{
		return [];
	}
}
