package com.schedule.mitso

class Converter{
    companion object {
        fun wordToLink(url: String): String{
            val mUrl = url.toCharArray()
            val v: ArrayList<Char> = ArrayList()
            for(i in 0 until mUrl.size){
                when(mUrl[i]){
                    'а' -> v.add('a')
                    'б' -> v.add('b')
                    'в' -> v.add('v')
                    'г' -> v.add('g')
                    'д' -> v.add('d')
                    'е' -> v.add('e')
                    'ж' -> {
                        v.add('z')
                        v.add('h')}
                    'з' -> v.add('z')
                    'и' -> v.add('i')
                    'й' -> v.add('j')
                    'к' -> v.add('k')
                    'л' -> v.add('l')
                    'м' -> v.add('m')
                    'н' -> v.add('n')
                    'о' -> v.add('o')
                    'п' -> v.add('p')
                    'р' -> v.add('r')
                    'с' -> v.add('s')
                    'т' -> v.add('t')
                    'у' -> v.add('u')
                    'ф' -> v.add('f')
                    'х' -> {
                        v.add('k')
                        v.add('h')}
                    'ц' -> {
                        v.add('t')
                        v.add('s')}
                    'ч' -> {
                        v.add('c')
                        v.add('h')}
                    'ш' -> {
                        v.add('s')
                        v.add('h')}
                    'щ' -> {
                        v.add('s')
                        v.add('h')
                        v.add('h')}
                    'ъ' -> {}
                    'ы' -> v.add('y')
                    'ь' -> v.add('`')
                    'э' -> {
                        v.add('e')
                        v.add('%')
                        v.add('6')
                        v.add('0')}
                    'ю' -> {
                        v.add('y')
                        v.add('u')}
                    'я' -> {
                        v.add('y')
                        v.add('a')}
                    ' ' -> {
                        v.add('%')
                        v.add('2')
                        v.add('0')}
                    '/' -> {
                        v.add('s')
                        v.add('l')
                        v.add('s')
                    }

                    'А' -> v.add('a')
                    'Б' -> v.add('b')
                    'В' -> v.add('v')
                    'Г' -> v.add('g')
                    'Д' -> v.add('d')
                    'Е' -> v.add('e')
                    'Ж' -> {
                        v.add('z')
                        v.add('h')}
                    'З' -> v.add('z')
                    'И' -> v.add('i')
                    'Й' -> v.add('j')
                    'К' -> v.add('k')
                    'Л' -> v.add('l')
                    'М' -> v.add('m')
                    'Н' -> v.add('n')
                    'О' -> v.add('o')
                    'П' -> v.add('p')
                    'Р' -> v.add('r')
                    'С' -> v.add('s')
                    'Т' -> v.add('t')
                    'У' -> v.add('u')
                    'Ф' -> v.add('f')
                    'Х' -> {
                        v.add('k')
                        v.add('h')}
                    'Ц' -> {
                        v.add('t')
                        v.add('s')}
                    'Ч' -> {
                        v.add('c')
                        v.add('h')}
                    'Ш' -> {
                        v.add('s')
                        v.add('h')}
                    'Щ' -> {
                        v.add('s')
                        v.add('h')
                        v.add('h')}
                    'Ъ' -> {}
                    'Ы' -> v.add('y')
                    'Ь' -> v.add('`')
                    'Э' -> {
                        v.add('e')
                        v.add('%')
                        v.add('6')
                        v.add('0')}
                    'Ю' -> {
                        v.add('y')
                        v.add('u')}
                    'Я' -> {
                        v.add('y')
                        v.add('a')}
                    else -> v.add(mUrl[i])
                }
            }
            return v.joinToString("")
        }

//        fun getLink(text: String): String{
//            var url: String = ""
//            when(text){
//                "МЭОиМ" -> url = "ME%60OiM"
//                "Магистратура" -> url = "Magistratura"
//                "Юридический" -> url = "YUridicheskij"
//                "Дневная" -> url = "Dnevnaya"
//                "Заочная" -> url = "Zaochnaya"
//                "Заочная сокр." -> url = "Zaochnaya%20sokrashhennaya"
//            }
//            return url
//        }
        fun time(text: String?): String{
            return if (text != null){
                val arr = text.split("-")
                arr[0]+" - "+arr[1]
            } else ""
        }
    }
}