# CurrencySearch

# Installation instructions
- Download the `app-release.apk` file [here](app/release/app-release.apk).

# Using the app
- On installation of the app, a sample list is loaded by default.
- This app supports light, dark and dynamic theme.

## DemoActivity
### Actions
In the actions section, you can click on the following buttons:
- Clear: Clears the database of all existing currencies
- Insert: Navigates to the insert screen, where you can input a JSON array for insertion into the database
- Crypto: Displays currency list containing only cryptocurrencies
- Fiat: Displays currency list containing only fiat currencies
- Both: Displays currency list containing both crypto and fiat currencies

### CurrencyListFragment
This component contains the list of currencies and a search bar that filters the list by the search query.

<img src="/search_demo.gif" width="30%" />

## Insertion
- When you click on insert, you will be redirected to the insert screen.
- There are 5 options:
    - List A: Loads sample list of cryptocurrencies
    - List B: Loads sample list of fiat currencies
    - List A & B: Loads sample list of cryptocurrencies and fiat currencies
    - Beautify: Adjusts indentation of the JSON array of currencies that you input
    - Clear: Clears the input in the input box

<img src="/insert_demo.gif" width="30%" />