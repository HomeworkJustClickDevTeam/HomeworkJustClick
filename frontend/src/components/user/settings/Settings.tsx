import {Link} from "react-router-dom";

export default function Settings(): JSX.Element{
    return(
        <>
            Ustawienia
            <ul>
                <li>
                    <Link to={"/settings/general"}>
                        Ogólne
                    </Link>
                </li>
                <li>Wygląd</li>
                <li>
                    <Link to={"/settings/security"}>
                        Bezpieczeństwo
                    </Link>
                </li>
                <li>Moje tabele ocen</li>
            </ul>
        </>
    )
}