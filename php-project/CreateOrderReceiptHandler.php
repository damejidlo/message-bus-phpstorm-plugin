<?php
declare(strict_types = 1);

namespace Damejidlo\Orders\Receipt;

use Damejidlo\CommandBus\ICommandHandler;



final class CreateOrderReceiptHandler implements ICommandHandler
{

	public function handle(CreateOrderReceiptCommand $command) : void
	{

	}

}
