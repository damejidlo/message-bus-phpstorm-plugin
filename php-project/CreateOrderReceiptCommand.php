<?php
declare(strict_types = 1);

namespace Damejidlo\Orders\Receipt;

use Damejidlo\CommandBus\ICommand;



/**
 * @see CreateOrderReceiptHandler
 */
final class CreateOrderReceiptCommand implements ICommand
{

	/**
	 * @var int
	 */
	private $orderId;



	public function __construct(int $orderId)
	{
		$this->orderId = $orderId;
	}



	public function getOrderId() : int
	{
		return $this->orderId;
	}



	public function toArray() : array
	{
		return [];
	}

}
