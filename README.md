# promotion-engine

### Overview

Promotion Engine is an application built for a cart checkout process. It runs on the existing cart
containing different types of items identified by their SKU Ids like below and each SKU Ids has a unit 
price attached to it.

| SKU Id  | Unit Price |
|---------|------------|
| A       | 50         |
| B       | 30         |
| C       | 20         |
| D       | 15         |

Promotion Engine can occupy multiple promotions and each promotion can be of either of the two category
=> Individual / Group. For ex:

* buy &#39;n&#39; items of a SKU for a fixed price (3 A&#39;s for 130) [ 3 of A&#39;s for 130 ] => Individual
* buy SKU 1 &amp; SKU 2 for a fixed price ( C + D = 30 ) [ C &amp; D for 30 ] => Group

### Functionality Provided

* Price computation on applying Group promotions for cart.
* Price computation on applying Individual promotions for cart.
* Price computation for remaining units after applying promotions for cart.
* End-point for add promotions by providing either promotion price / unit price percentage.
* End-point for computing checkout price on cart.

### Additional Technical Details

* Added support for both `local` (in-memory) and `remote` (h2 database) and can be easily plugged in with other 
remote databases if required.
* Profiles are maintained for `local` and `remote` connections.

### How to run ??

* Checkout the code.
* Run `mvn clean install`.
* Run `mvn spring-boot:run`.