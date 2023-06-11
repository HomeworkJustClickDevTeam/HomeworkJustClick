import { useContext, useState } from "react";
import userContext from '../../../UserContext'
import Loading from "../../animations/Loading";
import {newCredentials} from "../../../types/types"
import postgresqlDatabase from "../../../services/postgresDatabase";
import { AxiosError } from "axios";

export default function Security(): JSX.Element{
    const {loggedIn, userState} = useContext(userContext);
    const [loading, setLoading] = useState<boolean>(false)
    const [newCredentials, setNewCredentials] = useState<newCredentials>({
        "email": undefined,
        "password": undefined,
        "newPassword": undefined})
    const [newPasswordAppr, setNewPasswordAppr] = useState("")

    const handleSubmit = async (event:React.FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        console.log(newCredentials)
        if((newCredentials.newPassword === newPasswordAppr) && ((typeof newCredentials.password || typeof newCredentials.newPassword || typeof newPasswordAppr) !== undefined)){
            await postgresqlDatabase
                .post("/auth/changePassword",
                newCredentials)
                .catch((error:AxiosError) => {
                    console.log(error)
                })
        }
    }
    if(loading){
        return <Loading></Loading>
    }
    if (!loggedIn) {
        return <>Not log in</>
    }
    return(
        <form onSubmit={handleSubmit}>
            <label htmlFor="emailVerification">Email: </label>
            <input type="email" id="emailVerification" name="password"
                   onChange={
                        (event) =>{
                            setNewCredentials((prevState) =>(
                                {
                                    ...prevState,
                                    "email": event.target.value
                                }))}}/>
            <br/>
            <label htmlFor="oldPassword">Stare hasło: </label>
            <input type="password" id="oldPassword" name="password"
                onChange={
                    (event) => {
                        setNewCredentials((prevState) => ({
                            ...prevState,
                            "password": event.target.value
                        }))}
                }/>
            <br/>
            <label htmlFor="newPassword">Nowe hasło: </label>
            <input type="password" id="newPassword" name="password"
                onChange={
                    (event) => {
                        setNewCredentials((prevState) => ({
                            ...prevState,
                            "newPassword": event.target.value
                        }))}
                     }/>
            <br/>
            <label htmlFor="newPasswordApproval">Potwierdź nowe hasło: </label>
            <input type="password" id="newPasswordApproval" name="passwordApproval" onChange={(event) => {setNewPasswordAppr(event.target.value)}}/><br/>
            <input type="submit" value="Potwierdź"/>
        </form>
    )
}