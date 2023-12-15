package interpreter

import cats.data.State
import domain.{Product, ShoppingCardDsl, ShoppingCart, SimpleTaxationStrategy}

object InMemoryShoppingCartInterpreter {
  type ShoppingCartRepo = Map[String, ShoppingCart]
  type ShoppingCartRepoState[A] = State[ShoppingCartRepo, A]

  object ShoppingCardDslInterpreter extends ShoppingCardDsl[ShoppingCartRepoState] {
    override def create(id: String): ShoppingCartRepoState[Unit] = State.modify(_ + (id -> ShoppingCart(id, List.empty)))

    override def find(id: String): ShoppingCartRepoState[Option[ShoppingCart]] = State.inspect(_.get(id))

    override def addItems(cart: ShoppingCart, product: Product): ShoppingCartRepoState[ShoppingCart] = {
      State {
        carts => {
          val updatedProducts = product :: cart.items
          val newState = cart.copy(items = updatedProducts)
          (carts + (cart.id -> newState), newState)
        }
      }
    }

    override def calculateTotal(cart: ShoppingCart, taxRate: SimpleTaxationStrategy) = {
      cart.cartTotal + (cart.cartTotal * taxRate.rate/100)
    }
  }
}
