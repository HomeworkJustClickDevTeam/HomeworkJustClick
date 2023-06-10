import { Link } from "react-router-dom"
import "../button/button.css"
import "./HomeGuest.css"
import KolkoPoLewejStronie from './kolko_po_lewej_stronie.svg';
import KolkoPoPrawejStronie from './kolko_po_prawej_stronie.svg';
function HomeGuest() {
  return (
    <>
      <div className="kolkoPoLewejStronie">
        <img src={KolkoPoLewejStronie} alt="Kołko po lewej stronie" />
      </div>
      <div className="kolkoPoPrawejStronie">
        <img src={KolkoPoPrawejStronie} alt="Kółko po prawej stronie" />
      </div>
      <div className="homeworkJustClick">Homework JustClick!</div>
      <div className="prostoSzybkoWygodnie">Prosto, szybko, wygodnie!</div>
      <div className="nieMaszJeszczeKonta">Nie masz jeszcze konta?</div>
      <Link to="/login">
        <button className="button-login-pos button-filled button-filled-text button-size" type="button">Zaloguj się</button>
      </Link>
      <Link to="/register">
        <button className="button-register-pos button-outlined button-outlined-text button-size" type="button">Zarejestruj się</button>
      </Link>
    </>
  )
}
export default HomeGuest
