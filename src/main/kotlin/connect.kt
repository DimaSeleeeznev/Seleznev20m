import com.mongodb.ExplainVerbosity
import com.mongodb.client.FindIterable
import org.json.JSONObject
import org.litote.kmongo.KMongo
import org.litote.kmongo.json
val client = KMongo
    .createClient("mongodb://root:Ytns2VYGgszb@192.168.0.116:27017")

val mongoDatabase = client.getDatabase("test")
fun prettyPrintJson(json: String) =
    println(
        JSONObject(json)
            .toString(4)
    )

fun prettyPrintCursor(cursor: Iterable<*>) = prettyPrintJson("{ result: ${cursor.json} }")