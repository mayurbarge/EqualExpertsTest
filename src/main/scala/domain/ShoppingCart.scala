package domain

final case class Product(name: String, unitPrice: BigDecimal)
final case class ShoppingCart(id: String, items: List[Product]) {
  def cartTotal = this.items.map(_.unitPrice).sum
}
