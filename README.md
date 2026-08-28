# Appok - Android App Launcher

Egy egyszerű Android alkalmazásindító, amely lehetővé teszi az alkalmazások keresését, szerkesztését és szervezését.

## Funkciók

- 🔍 **Alkalmazás keresés**: Könnyen kereshetsz az összes telepített alkalmazás között
- 📱 **Testreszabható rács**: Válassz 3×3, 4×4 vagy 5×5-ös rácsok között
- 📄 **Lapozás**: Több lap a több alkalmazáshoz
- ⚙️ **Menü**: Elrendezés, lapok és rács méretének szerkesztése
- 🚀 **Gyors indítás**: Kattints az alkalmazásra az elindításhoz

## UI Elrendezés

### Alsó sáv (60dp magasság)
- **Bal sarokban** (30%): Kilépés gomb (×)
- **Közepén** (60%): Keresőmező
- **Jobb sarokban** (10%): Menü gomb (⋮)

## Telepítés

```bash
git clone https://github.com/rolpur00/appok.git
cd appok
./gradlew build
./gradlew installDebug
```

## Követelmények

- Android 5.0 (API 21) vagy magasabb
- Kotlin 1.9.0+

## Licenc

MIT License
