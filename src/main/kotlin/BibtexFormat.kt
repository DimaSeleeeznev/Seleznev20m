import kotlinx.serialization.Serializable
@Serializable
data class BibtexFormat(
    val type:String, //@*type*{ ... ----book, manual, article etc
    val ID:String, // @*type*{ **bookID**, ...
    val journal:String? = null,
    val title:String, //title = { ... }
    val DOI:String? = null, //DOI = { ... } / DOI == digital object identifier
    val volume:String? = null,
    val pages:String? = null,
    val author:String, //author = { ... } ----may contain multiple authors && keys ""and"", "",""
    val year:String? = null, // year = { ... }
    val publisher:String? = null, // publisher = { ... }
    val langid:String? = null, // langid = { ... }
    val url:String? = null, //url = { ... }
    val number:String? = null //number = { ... }
)
