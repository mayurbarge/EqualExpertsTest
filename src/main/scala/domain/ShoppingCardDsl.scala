package domain

trait ShoppingCardDsl[F[_]] {
  def create(id: String): F[Unit]
  def find(id: String): F[Option[ShoppingCart]]
  def addItems(cart: ShoppingCart, product: Product): F[ShoppingCart]
  def calculateTotal(cart: ShoppingCart, taxRate: SimpleTaxationStrategy): BigDecimal
}

