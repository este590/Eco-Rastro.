package com.ecorastro.app

import android.app.Activity
import android.os.Bundle
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.*

class MainActivity : Activity() {
    private val green = Color.rgb(24, 79, 69)
    private val leaf = Color.rgb(47, 121, 87)
    private val cream = Color.rgb(249, 246, 238)
    private val ink = Color.rgb(38, 67, 61)
    private lateinit var root: LinearLayout
    private lateinit var content: LinearLayout
    private val prefs by lazy { getSharedPreferences("ecorastro", Context.MODE_PRIVATE) }
    private var points: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        points = prefs.getInt("points", 0)
        showHome()
    }

    private fun bg(color: Int, radius: Float = 18f): GradientDrawable =
        GradientDrawable().apply { setColor(color); cornerRadius = radius * resources.displayMetrics.density }

    private fun base(title: String, subtitle: String? = null) {
        root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setBackgroundColor(cream) }
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL; setPadding(22, 22, 22, 18); setBackgroundColor(green)
        }
        header.addView(label("ECO-RASTRO", 23f, Color.WHITE, true))
        header.addView(label(title, 18f, Color.WHITE, true).apply { setPadding(0, 14, 0, 0) })
        if (subtitle != null) header.addView(label(subtitle, 13f, Color.rgb(218,235,220), false))
        root.addView(header)
        val scroll = ScrollView(this)
        content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(18,18,18,24) }
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        val nav = LinearLayout(this).apply { setPadding(5,8,5,8); setBackgroundColor(Color.WHITE); gravity = Gravity.CENTER }
        listOf("Inicio", "Registro", "Reciclar", "Comunidad").forEach { item ->
            val b = TextView(this).apply {
                text = item; textSize = 11f; gravity = Gravity.CENTER; setTextColor(green)
                setPadding(7,10,7,10); setTypeface(null, Typeface.BOLD)
                setOnClickListener { when(item) { "Inicio" -> showHome(); "Registro" -> showRegister(); "Reciclar" -> showGuide(); else -> showCommunity() } }
            }
            nav.addView(b, LinearLayout.LayoutParams(0, -2, 1f))
        }
        root.addView(nav)
        setContentView(root)
    }

    private fun label(text: String, size: Float, color: Int = ink, bold: Boolean = false): TextView =
        TextView(this).apply { this.text=text; textSize=size; setTextColor(color); if (bold) setTypeface(null, Typeface.BOLD) }

    private fun card(): LinearLayout = LinearLayout(this).apply {
        orientation=LinearLayout.VERTICAL; setPadding(16,15,16,15); background=bg(Color.WHITE)
    }

    private fun addCard(view: View) {
        val lp = LinearLayout.LayoutParams(-1, -2); lp.bottomMargin=13; content.addView(view, lp)
    }

    private fun action(text: String, click: () -> Unit): Button = Button(this).apply {
        this.text=text; isAllCaps=false; setTextColor(Color.WHITE); background=bg(leaf, 14f)
        setOnClickListener { click() }
    }

    private fun showHome() {
        base("Tu huella deja un buen rastro", "Pequeñas acciones crean grandes cambios.")
        val hero = card().apply {
            setBackgroundColor(green)
            addView(label("¡Hola, eco-amigo! 🌿", 21f, Color.WHITE, true))
            addView(label("Cuida el planeta un hábito a la vez.", 14f, Color.WHITE).apply { setPadding(0,7,0,12) })
            addView(label("$points PUNTOS ECO", 25f, Color.rgb(208,235,177), true))
            addView(label("Sigue registrando tus acciones para subir de nivel.", 12f, Color.WHITE))
        }
        addCard(hero)
        val stats = card()
        stats.addView(label("Tu impacto de hoy", 18f, green, true))
        stats.addView(label("💧 Agua  •  ⚡ Energía  •  ♻️ Residuos", 14f).apply { setPadding(0,10,0,5) })
        stats.addView(label("Registra tus hábitos y observa tu progreso.", 13f))
        addCard(stats)
        val features = listOf(
            Triple("📝 Registro diario", "Anota agua, energía y residuos.", { showRegister() }),
            Triple("♻️ Guía de reciclaje", "Aprende a separar correctamente.", { showGuide() }),
            Triple("🏅 Retos y recompensas", "Gana puntos con acciones positivas.", { showRewards() }),
            Triple("🌎 Comunidad", "Comparte tus acciones ambientales.", { showCommunity() })
        )
        features.forEach { (title, desc, click) ->
            val c=card(); c.addView(label(title,17f,green,true)); c.addView(label(desc,13f).apply { setPadding(0,5,0,9) }); c.addView(action("Abrir", click))
            addCard(c)
        }
    }

    private fun showRegister() {
        base("Registro diario", "Registra tus hábitos de manera sencilla.")
        val c=card()
        c.addView(label("¿Qué acción realizaste hoy?",19f,green,true))
        val water = EditText(this).apply { hint="Agua utilizada (litros, aproximado)"; inputType=2 }
        val energy = EditText(this).apply { hint="Energía utilizada (kWh, aproximado)"; inputType=8194 }
        val waste = EditText(this).apply { hint="Residuos generados (kg, aproximado)"; inputType=8194 }
        listOf(water,energy,waste).forEach { c.addView(it) }
        c.addView(action("Guardar registro y ganar 10 puntos",) {
            val has = listOf(water,energy,waste).any { it.text.toString().isNotBlank() }
            if (!has) Toast.makeText(this,"Escribe al menos un dato.",Toast.LENGTH_SHORT).show()
            else {
                points += 10; prefs.edit().putInt("points",points).putString("last_record", "${water.text}|${energy.text}|${waste.text}").apply()
                Toast.makeText(this,"Registro guardado. +10 puntos ECO",Toast.LENGTH_LONG).show(); showRegister()
            }
        })
        addCard(c)
        val h=card(); h.addView(label("Tu último registro",18f,green,true))
        h.addView(label(prefs.getString("last_record","Aún no tienes registros.") ?: "Aún no tienes registros.",14f))
        addCard(h)
    }

    private fun showGuide() {
        base("Guía de reciclaje", "Separa tus residuos para darles una nueva oportunidad.")
        val items = listOf(
            "🔵 PAPEL Y CARTÓN" to "Hojas, periódicos y cajas limpias y secas. Evita papel mojado o con grasa.",
            "🟡 PLÁSTICOS Y METALES" to "Envases, botellas y latas vacías. Enjuaga y aplasta cuando sea posible.",
            "🟢 VIDRIO" to "Frascos y botellas de vidrio. Maneja con cuidado y separa los objetos rotos.",
            "🟤 ORGÁNICOS" to "Restos de frutas, verduras y hojas. Pueden aprovecharse para composta.",
            "⚫ NO RECICLABLES" to "Residuos sanitarios y materiales contaminados. Consulta las reglas locales."
        )
        items.forEach { (t,d) -> val c=card(); c.addView(label(t,17f,green,true)); c.addView(label(d,14f).apply { setPadding(0,7,0,0) }); addCard(c) }
        val c=card(); c.addView(label("Puntos de recolección",18f,green,true)); c.addView(label("Consulta los centros de acopio, horarios y materiales aceptados en tu municipio antes de acudir.",14f).apply { setPadding(0,7,0,0) }); addCard(c)
    }

    private fun showRewards() {
        base("Retos y recompensas", "Cada hábito cuenta.")
        val c=card(); c.addView(label("Tus puntos ECO",18f,green,true)); c.addView(label("$points puntos",30f,leaf,true)); c.addView(label("Los puntos se acumulan al guardar tus registros diarios.",13f)); addCard(c)
        listOf("💧 Guardián del agua — registra acciones para ahorrar agua.","⚡ Ahorrador de energía — apaga luces y desconecta lo que no uses.","♻️ Reciclador experto — separa correctamente tus residuos.","🌱 Hábito verde — realiza una acción ecológica cada día.").forEach {
            val x=card(); x.addView(label(it,15f,green,true)); addCard(x)
        }
        addCard(card().apply { addView(label("Nivel actual",18f,green,true)); addView(label(if(points>=100)"Eco experto" else if(points>=40)"Eco comprometido" else "Eco principiante",20f,leaf,true)) })
    }

    private fun showCommunity() {
        base("Muro comunitario", "Inspira a otras personas con tus acciones.")
        val c=card(); c.addView(label("Comparte una acción ambiental",18f,green,true))
        val post=EditText(this).apply { hint="Hoy ayudé al planeta haciendo..." ; minLines=3; gravity=Gravity.TOP }
        c.addView(post)
        c.addView(action("Publicar",) {
            val value=post.text.toString().trim()
            if(value.isEmpty()) Toast.makeText(this,"Escribe tu acción antes de publicar.",Toast.LENGTH_SHORT).show()
            else { prefs.edit().putString("community_post",value).apply(); Toast.makeText(this,"¡Publicación guardada!",Toast.LENGTH_SHORT).show(); showCommunity() }
        })
        addCard(c)
        val saved=prefs.getString("community_post",null)
        if(saved!=null) { val p=card(); p.addView(label("Tu publicación",17f,green,true)); p.addView(label(saved,14f).apply { setPadding(0,8,0,0) }); addCard(p) }
        val tip=card(); tip.addView(label("Consejo ECO",18f,green,true)); tip.addView(label("Lleva una bolsa reutilizable, separa tus residuos y comparte estos hábitos con tu familia y comunidad.",14f).apply { setPadding(0,7,0,0) }); addCard(tip)
    }
}