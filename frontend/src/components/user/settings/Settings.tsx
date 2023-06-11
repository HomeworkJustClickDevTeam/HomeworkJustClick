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
                <li><Link to={"/settings/appearance"}>Wygląd</Link></li>
                <li>
                    <Link to={"/settings/security"}>
                        Bezpieczeństwo
                    </Link>
                </li>
                <li><Link to={"/settings/markingTables"}>Moje tabele ocen</Link></li>
            </ul>
        </>
    )
}