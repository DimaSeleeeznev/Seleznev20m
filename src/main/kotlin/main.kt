import com.mongodb.client.FindIterable
import com.mongodb.client.model.UpdateOptions
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import kotlinx.serialization.Serializable
import org.apache.pdfbox.pdmodel.interactive.viewerpreferences.PDViewerPreferences
import org.apache.poi.ss.formula.functions.Count
import org.litote.kmongo.*
import org.litote.kmongo.serialization.LocalDateSerializer
import java.io.File
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
//Подключение Дб

fun main() {
    // prettyPrintCursor(mInfo.find())
    print("Введите имя интересующего вас профессора: ")
    var nameProfessor = readLine()!! //Имя профессора на вход
    print("Введите ваше ФИО: ")
    var yourName = readLine() //ФИО соискателя
    print("Введите ФИО заведующего кафедрой: ")
    var zavKafedroi = readLine() //ФИО Зав. кафедрой
    print("Введите ФИО Ученого секретаря ученого совета: ")
    var uchSekret = readLine() //ФИО ученого секретаря ученого совета
    val sdf = SimpleDateFormat("dd.MM.yyyy") //Настройка шаблона вывода данных
    val currentDate = sdf.format(Date()) //Запись даты по шаблону
    //Стили для элементов
    var styleTable = "border: 2px solid;text-align:center; width: 900px; margin:auto; border-collapse: collapse;"
    var border = "border: 2px solid;"
    var splitAuthors: List<String> //Список для хранения разделенного поляAuthors
    var splitRegex = "( and )|, ".toRegex() //Регулярное выражение для деленияполя Authors на отдельных авторов
    var form: String //Форма изданий и науч трудов
    var count = 0 //Переменная для введения счета записей
    val mInfo = mongoDatabase.getCollection<BibtexFormat>()
    var lol = mInfo.find().toList()
    val listCheck = mutableListOf<BibtexFormat>()
    for (i in lol.indices){
        if (nameProfessor in lol[i].toString()){
            listCheck.add(lol[i])
        }
    }
    println(listCheck) //Проверка поиска
    File("form16.html").writeText( //Работа с файлом
        StringBuilder().appendHTML().html {//appendHTML()
            head {
                title { +"Form16" } //Имя вкладки
                meta {
                    charset = "windows-1251" //Кодировка - windows-1251
                }
            }
            body {
                h1 { //Заголовок 1
                    style = "text-align:center" //Выравнивание текста по центру
                    +"Список"
                }
                h2 { // Заголовок 2
                    style = "text-align:center"
                    +"Опубликованных учебных изданий и научных трудов"
                    br
                    +"Профессора $nameProfessor"
                }
                table {// Таблица с данными
                    style = styleTable //Редактирование вида таблицы
                    tr { //Создание строки таблицы
                        // Шапка таблицы
                        style = border
                        td { //Создание ячейки таблицы
                            style = border
                            +"№"
                        }
                        td {
                            style = border
                            +"Наименование учебных публикаций, научных работ и патентов на изобретения и другие объекты интеллектуальной собственности"
                        }
                        td {
                            style = border
                            +"Форма учебных изданий и научных трудов"
                        }
                        td {
                            style = border
                            +"Выходные данные"
                        }
                        td {
                            style = border
                            +"Объем"
                        }
                        td {
                            style = border
                            +"Соавторы"
                        } //Конец шапки таблицы
                    }
                    listCheck.forEach {
                        count++
                        tr {
                            style = border
                            td { //Номер строки
                                style = border
                                +"$count."
                            }
                            td { //Наименование учебных работ
                                style = border
                                style = "text-align:left"
                                +it.title
                                br
                                div {
                                    style = "font-style: italic"
                                    +"(${it.type}"
                                    if (it.langid != null) +",${it.langid})" else +")"
                                }
                            }
                            if (it.url == null) {
                                form = "printed"
                                td { //Форма в которой представлены труды
                                    style = border
                                    +"Печатная"
                                }
                                td {// Выходные данные
                                    style = border
                                    style = "text-align: left"
                                    if (it.number != null) {
                                        if (it.year != null) {
                                            if (it.journal != null) {
                                                if (it.publisher != null) {
                                                    +"${it.publisher},${it.journal}. – ${it.year}.– № ${it.number}. – C. ${it.pages}."
                                                }
                                            } else +"${it.publisher}, –${it.year}.– № ${it.number}. – C. ${it.pages}."
                                        } else if (it.journal != null)
                                            +"${it.journal}. – № ${it.number}. – C. ${it.pages}."
                                    } else if (it.year != null) {
                                        if (it.journal != null) {
                                            +"${it.journal}. – ${it.year}."
                                        } else +"Year: ${it.year}."
                                    } else if (it.journal != null)
                                        +"${it.journal}."
                                }
                            } else {
                                form = "electronic"
                                td {
                                    style = border
                                    +"Электронная"
                                }
                                td {
                                    style = border
                                    if (it.year != null) {
                                        if (it.publisher != null) {
                                            if (it.journal != null) {
                                                +"Интернет-журнал\"${it.journal}\". – ${it.publisher}, ${it.year}. - URL: ${it.url}"
                                            } else +"Издание:${it.publisher}, ${it.year}. - URL: ${it.url}"
                                        } else if (it.journal != null)
                                            +"Интернет-журнал \"${it.journal}\". ${it.year}. - URL: ${it.url}"
                                        else +"${it.year}. - URL: ${it.url}"
                                    } else if (it.publisher != null) {
                                        if (it.journal != null) +"Интернетжурнал \"${it.journal}\". – ${it.publisher}, - URL: ${it.url}"
                                        else +"${it.publisher}, - URL:${it.url}"
                                    } else if (it.journal != null)
                                        +"Интернет-журнал \"${it.journal}\". - URL: ${it.url}"
                                    else +"URL: ${it.url}"
                                }
                            }
                            td {
                                style = border
                                if (form == "printed")
                                    if (it.volume != null) +"${it.volume}СТР."
                                if (form == "electronic")
                                    if (it.volume != null) +"${it.volume}МБ."
                            }
                            td {
                                style = border
                                style = "text-align:left"
                                splitAuthors = it.author.split(splitRegex)
                                if (splitAuthors.count() > 1)
                                    for (i in splitAuthors.indices) {
                                        if (splitAuthors[i] !=
                                            nameProfessor)
                                            if (splitAuthors.count() < 6) {
                                                if (i ==
                                                    splitAuthors.lastIndex) +"${splitAuthors[i]}." else +"${splitAuthors[i]}, "
                                            } else {
                                                if (i < 5)
                                                    +"${splitAuthors[i]}, "
                                                if (i == 5) {
                                                    +"${splitAuthors[i]} "
                                                    +" and other, all${splitAuthors.count() - 1}"
                                                }
                                            }
                                    }
                            }
                        }
                    }
                }
//Записи в конце файла
                table {
                    style = "width: 900px; margin: auto"
                    tr {
                        td {
                            style = "text-align: left"
                            +"Соискатель"
                        }
                        td {
                            style = "text-align: right"
                            +"______________$yourName"
                        }
                    }
                    tr {
                        td {
                            style = "text-align: left"
                            +"Зав. кафедрой ____________"
                        }
                        td {
                            style = "text-align: right"
                            +"______________$zavKafedroi"
                        }
                    }
                    tr {
                        td {
                            style = "text-align: left"
                            +"Ученый секретарь ученого совета"
                        }
                        td {
                            style = "text-align: right"
                            +"______________$uchSekret"
                        }
                    }
                    tr {
                        td {
                            style = "text-align: left"
                            +"$currentDate"
                        }
                    }
                }
            }
        }.toString(),
        charset("windows-1251")) //Кодировка итогового файла
}