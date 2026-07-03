package com.example

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val oldPrice: Double? = null,
    val rating: Double,
    val reviews: Int = 0,
    val imageResUrl: String = "",
    val isGeneric: Boolean = false,
    val isBestSeller: Boolean = false,
    val category: String = "",
    val formFactor: String = "", // e.g. "Cardiology", "Tablets", "Capsules - 30 Count"
    val dosage: String = "",
    val details: String = "",
    val safetyInfo: List<String> = emptyList()
)

data class CartItem(
    val product: Product,
    val quantity: Int
)

enum class OrderStatus {
    DELIVERED, PENDING_SIGNATURE, PROCESSING
}

data class Order(
    val id: String,
    val date: String,
    val itemsDesc: String,
    val total: Double,
    val status: OrderStatus
)

val sampleProducts = listOf(
    Product(
        id = "p1",
        name = "Paracetamol 500mg",
        description = "Standard paracetamol tablets for everyday pain relief. Pack of 10.",
        price = 5.99,
        rating = 4.8,
        reviews = 120,
        isGeneric = true,
        category = "Fever",
        formFactor = "Tablets - 30 Count",
        details = "Pain relief formulation.",
        imageResUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB057cWo5mxjC6o0isMlz7TIX5-ToaOb5ZxYtTl1p4eIbK_u3HdM40TDBQnD7JNIycLFmpgBxOOXY3HQyGjfbeFuKrtBHMQXLcw4Y6Mv8sKBO3hWxjfoAF6MG0RKDd0qsIfBhM7y-oKswj8Lss4z5Vb3kWwQhltMFpmsxrS6wv8Y59O54UekaqrdCvRSoZibdp1EMO7iTp3hHdeeGqEIMkyV33K0E2bZYlqzDxy-kOtwwu2ov4WA3uU3NdJBaqxH1ZZzKgOZqRcGbqz"
    ),
    Product(
        id = "p2",
        name = "Amoxicillin 250mg",
        description = "Antibiotic capsules.",
        price = 12.50,
        rating = 4.5,
        reviews = 85,
        category = "Antibiotic",
        formFactor = "Capsules - 30 Count",
        details = "Common antibiotic.",
        imageResUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCUzuSbf-Q3WEiQU4gOKV9QgrUbvTVvYCyN5LTBkGhIEa-mKHpblyLSUkMr3R_BbEukaAJepGGc-Sko-9kv2HdzZyjnKHKwkOUmuaIgHmBJ7Ywg1SDQght2vtjsBrLs0Xx_cvZHOTzn_OJEuKr-BrBAscXrMZaQQb1Eqvq1ArrkPnj-YJePHOQIzyQS42e4XmUNYVv-gqGb76e0LTgKKQE-b6OY1EEOF-WcaYbgmXsQBHrzQeW-1-kfXRIjZq8KAQbJsV0T83ROmCSz"
    ),
    Product(
        id = "p3",
        name = "Vitamin C 1000mg",
        description = "Vitamin supplement.",
        price = 8.00,
        rating = 4.9,
        reviews = 210,
        category = "Supplement",
        formFactor = "Tablets - 60 Count",
        details = "Vitamin C boost.",
        imageResUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCxuynq9xBwsT_hzswQe1h71ZEBCkWqzgn_6r35HoZs5tz_-x4QfyYZJx7nN1Uc_FxM0bzMcAPXeuJR9gfsFOGzFy_IUAkLiq2VgKCQ3FwIAwSnv-jXU57mYmpbg8Y8eQest-j98Nz2Yp7mK1OvIzurtViVHZKCS9dyWJ-TCMIhSvNPLpebmizZhLHPdta3ViHphgM49ZLEPbwk2jwoMczldDZNrHnY-GE2li0ahE8eX4TQHGFEz31sPfcnIYU5-1rsJNREPsexBpQE"
    ),
    Product(
        id = "p4",
        name = "Amlodipine Besylate 5mg",
        description = "Calcium channel blocker used to treat high blood pressure.",
        price = 14.99,
        rating = 4.7,
        reviews = 42,
        category = "Cardiology",
        formFactor = "Tablets",
        details = "Amlodipine is a calcium channel blocker used to treat high blood pressure (hypertension) and chest pain (angina). Lowering high blood pressure helps prevent strokes, heart attacks, and kidney problems. It works by relaxing blood vessels so blood can flow more easily.",
        dosage = "Take one 5mg tablet daily, with or without food. Follow your doctor's specific instructions. Do not crush or chew the tablet.",
        safetyInfo = listOf(
            "May cause swelling of the ankles or feet.",
            "Dizziness or lightheadedness may occur.",
            "Consult doctor if you experience rapid heartbeat."
        ),
        imageResUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAtqRDHxAe0wip3Jb5fr9RVtMKYL7eCK0Hx9-zBdxa0yDlKg1sIMsA4wuiscgiU2Y3UNSBHqormZ6osuYzj3qG0MGH3-nHhE4xO5Nh5Kt6lrWrgqlEvGAcQf0l-Q9Lw0t6H-tbccLX2KLVl36jW8sZldZsofYqowGHCIvqX05pbIE-ov0PR3nFkYBrc6zZGSW_CwBzLldFl8zhFV1wEDBC2xT6cSoNZ-4heIk23gsCYzbLbUpQdwTCYHZg10yajoT77Wx2boJtY26LF"
    ),
    Product(
        id = "p5",
        name = "Crocin Advance",
        description = "Fast release pain relief formulation. 500mg Paracetamol. Pack of 15 tablets.",
        price = 38.50,
        oldPrice = 45.00,
        rating = 4.8,
        reviews = 99,
        isBestSeller = true,
        category = "Pain Relief",
        formFactor = "Tablets - 15 Count",
        imageResUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBFAnpQZ47qAge-tAb04BEXrvjIQynb_oPaNGVque7pTxq3TO2CX40udJlFjTaTIsrXAhrBmlsiAY9IhKGHnx3ICtYdyjo58vG7f7H4ywFw9DWeqIW5gILJeigzgCss747jmIewPSEDxo8qnlRtSFDAKfqGXBlI3XxZyAl5c3_EY6oONWErMvNhesxku8ei9ZvPLbcl-x-773qNPufB1TFJ_jXXoXBY3VysbaouCTb1MdfhoadslZLUmfttMTQZrYusCNbzmf9YiRzn"
    ),
    Product(
        id = "p6",
        name = "Dolo 650",
        description = "Analgesic and antipyretic medicine. Used to treat mild to moderate pain and fever.",
        price = 30.91,
        oldPrice = 33.00,
        rating = 4.9,
        reviews = 150,
        category = "Pain Relief",
        formFactor = "Tablets - 10 Count",
        imageResUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBXv-e-ovsjFEl6ogYoT5NelSTsAvrxKOQ63Z9oNkbWMUz1lT-iMUwBG1YMue4I513QTp5k9s9QZHl4xUMvieEBnC4SgKL_v-lXWA-7mhd4gtd8flVAGKLbuKNFfEidfl4nCD9E69xKNT9Sbck64ATWWbprjszspRdsvCFImU70KxlHvKV0UcN7V9_F7TxB9iOZJikZ61blIS8kOyYBv2EOILh1M0Lq4X3IXux6-o6as9DyjrPBPJ16oLK9TMJldLLvBj88R-bjMuEI"
    ),
    Product(
        id = "p7",
        name = "Ibuprofen 400mg",
        description = "Effective relief from pain and inflammation.",
        price = 8.50,
        rating = 4.6,
        reviews = 310,
        category = "Pain Relief",
        formFactor = "Tablets - 20 Count",
        details = "Nonsteroidal anti-inflammatory drug (NSAID) used to treat pain, fever, and inflammation.",
        imageResUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAtqRDHxAe0wip3Jb5fr9RVtMKYL7eCK0Hx9-zBdxa0yDlKg1sIMsA4wuiscgiU2Y3UNSBHqormZ6osuYzj3qG0MGH3-nHhE4xO5Nh5Kt6lrWrgqlEvGAcQf0l-Q9Lw0t6H-tbccLX2KLVl36jW8sZldZsofYqowGHCIvqX05pbIE-ov0PR3nFkYBrc6zZGSW_CwBzLldFl8zhFV1wEDBC2xT6cSoNZ-4heIk23gsCYzbLbUpQdwTCYHZg10yajoT77Wx2boJtY26LF"
    ),
    Product(
        id = "p8",
        name = "Cetirizine 10mg",
        description = "Allergy relief tablets.",
        price = 6.20,
        rating = 4.7,
        reviews = 450,
        category = "Allergy",
        formFactor = "Tablets - 14 Count",
        details = "Antihistamine used to relieve allergy symptoms such as watery eyes, runny nose, itching eyes/nose, and sneezing.",
        imageResUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB057cWo5mxjC6o0isMlz7TIX5-ToaOb5ZxYtTl1p4eIbK_u3HdM40TDBQnD7JNIycLFmpgBxOOXY3HQyGjfbeFuKrtBHMQXLcw4Y6Mv8sKBO3hWxjfoAF6MG0RKDd0qsIfBhM7y-oKswj8Lss4z5Vb3kWwQhltMFpmsxrS6wv8Y59O54UekaqrdCvRSoZibdp1EMO7iTp3hHdeeGqEIMkyV33K0E2bZYlqzDxy-kOtwwu2ov4WA3uU3NdJBaqxH1ZZzKgOZqRcGbqz"
    ),
    Product(
        id = "p9",
        name = "Metformin 500mg",
        description = "Medication for type 2 diabetes.",
        price = 15.00,
        rating = 4.4,
        reviews = 120,
        category = "Diabetes",
        formFactor = "Tablets - 30 Count",
        details = "Used to treat high blood sugar levels that are caused by a type of diabetes mellitus or sugar diabetes.",
        imageResUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCUzuSbf-Q3WEiQU4gOKV9QgrUbvTVvYCyN5LTBkGhIEa-mKHpblyLSUkMr3R_BbEukaAJepGGc-Sko-9kv2HdzZyjnKHKwkOUmuaIgHmBJ7Ywg1SDQght2vtjsBrLs0Xx_cvZHOTzn_OJEuKr-BrBAscXrMZaQQb1Eqvq1ArrkPnj-YJePHOQIzyQS42e4XmUNYVv-gqGb76e0LTgKKQE-b6OY1EEOF-WcaYbgmXsQBHrzQeW-1-kfXRIjZq8KAQbJsV0T83ROmCSz"
    ),
    Product(
        id = "p10",
        name = "Pantoprazole 40mg",
        description = "Gastro-resistant tablets for acid reflux.",
        price = 11.30,
        rating = 4.8,
        reviews = 340,
        category = "Digestion",
        formFactor = "Tablets - 15 Count",
        details = "Proton pump inhibitor (PPI) that reduces the amount of acid produced in the stomach.",
        imageResUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCxuynq9xBwsT_hzswQe1h71ZEBCkWqzgn_6r35HoZs5tz_-x4QfyYZJx7nN1Uc_FxM0bzMcAPXeuJR9gfsFOGzFy_IUAkLiq2VgKCQ3FwIAwSnv-jXU57mYmpbg8Y8eQest-j98Nz2Yp7mK1OvIzurtViVHZKCS9dyWJ-TCMIhSvNPLpebmizZhLHPdta3ViHphgM49ZLEPbwk2jwoMczldDZNrHnY-GE2li0ahE8eX4TQHGFEz31sPfcnIYU5-1rsJNREPsexBpQE"
    ),
    Product(
        id = "p11",
        name = "Aspirin 75mg",
        description = "Low-dose aspirin for heart protection.",
        price = 4.50,
        rating = 4.9,
        reviews = 520,
        category = "Heart",
        formFactor = "Tablets - 28 Count",
        details = "Helps prevent heart attacks and strokes.",
        imageResUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBFAnpQZ47qAge-tAb04BEXrvjIQynb_oPaNGVque7pTxq3TO2CX40udJlFjTaTIsrXAhrBmlsiAY9IhKGHnx3ICtYdyjo58vG7f7H4ywFw9DWeqIW5gILJeigzgCss747jmIewPSEDxo8qnlRtSFDAKfqGXBlI3XxZyAl5c3_EY6oONWErMvNhesxku8ei9ZvPLbcl-x-773qNPufB1TFJ_jXXoXBY3VysbaouCTb1MdfhoadslZLUmfttMTQZrYusCNbzmf9YiRzn"
    ),
    Product(
        id = "p12",
        name = "Clindamycin 300mg",
        description = "Antibiotic for bacterial infections.",
        price = 18.20,
        rating = 4.3,
        reviews = 90,
        category = "Antibiotic",
        formFactor = "Capsules - 15 Count",
        details = "Lincosamide antibiotic used for the treatment of a number of bacterial infections.",
        imageResUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBXv-e-ovsjFEl6ogYoT5NelSTsAvrxKOQ63Z9oNkbWMUz1lT-iMUwBG1YMue4I513QTp5k9s9QZHl4xUMvieEBnC4SgKL_v-lXWA-7mhd4gtd8flVAGKLbuKNFfEidfl4nCD9E69xKNT9Sbck64ATWWbprjszspRdsvCFImU70KxlHvKV0UcN7V9_F7TxB9iOZJikZ61blIS8kOyYBv2EOILh1M0Lq4X3IXux6-o6as9DyjrPBPJ16oLK9TMJldLLvBj88R-bjMuEI"
    )
)

val sampleOrders = listOf(
    Order(
        id = "MS-8492-AX",
        date = "Oct 24, 2023",
        itemsDesc = "Lisinopril 20mg (30 tabs), Atorvastatin 40mg (30 tabs)",
        total = 42.50,
        status = OrderStatus.DELIVERED
    ),
    Order(
        id = "MS-9102-BY",
        date = "Oct 28, 2023",
        itemsDesc = "Amoxicillin 500mg (20 caps) - Prescription Required",
        total = 18.00,
        status = OrderStatus.PENDING_SIGNATURE
    ),
    Order(
        id = "MS-9255-CZ",
        date = "Nov 02, 2023",
        itemsDesc = "Daily Multivitamin Supplement (60 gummies), Vitamin D3",
        total = 35.99,
        status = OrderStatus.PROCESSING
    )
)
