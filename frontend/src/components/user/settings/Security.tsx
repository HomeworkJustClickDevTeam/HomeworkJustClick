export default function Security(): JSX.Element{



    return(
        <form>
            <label htmlFor="password">Zmień hasło: </label>
            <input type="text" id="password" name="password"/><br/>
            <label htmlFor="passwordApproval">Potwierdź nowe hasło: </label>
            <input type="text" id="passwordApproval" name="passwordApproval"/><br/>
            <input type="submit" value="Potwierdź"/>
        </form>
    )
}