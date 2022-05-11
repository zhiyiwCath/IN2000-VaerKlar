# Klesappen

## Arkitektur
Appen følger prinsippene for MVVM-arkitektur. Det innebærer at programmet er delt i tre: (1) model er representasjonen av de dataene vi jobber med som objekter, (2) viewmodel inneholder beregninger og logikk for hvordan data i model behandles slikt at det blir klart til presentasjon og (3) view gjør den faktiske grafiske fremvisningen av dataene.

I forbindelse med model, har vi API-kall og senere også database. Overordnet for å organisere disse og har vi også et såkalt repository. Repositoriet skal sikre at det ikke skjer unødvendige API-kall, vurdere når data er utgått og bør oppdateres og unngå dupliserte verdier.

Dette er i dag organisert i en mappestruktur:
Data:
- API, er implementert med et interface, slik at vi enkelt kan bytte ut med dummy-verdier for testing
- Model, mapper som inneholder dataklasser for de ulike API-kallene
- Repository: MainRepository.kt, videreformidler per nå kun funksjonskall videre til APIet, men senere vil den også lagre verdiene fra API i databasen.

UI:
- View: MainActivity.kt
- ViewModel: MainViewModel

## Oppsett av repoet i android studio
[Her er en god guide](https://github.uio.no/steffa/Ukesoppgaver-IN2000-V21/blob/master/Oppsett%20av%20GitHub.md "Oppsett av Github") for å få prosjektet opp å gå på egen maskin.
