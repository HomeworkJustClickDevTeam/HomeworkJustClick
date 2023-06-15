import { useState } from "react";
import {newCredentials} from "../../../types/types"
import postgresqlDatabase from "../../../services/postgresDatabase";
import { AxiosError } from "axios";

export default function UserSecuritySettings(): JSX.Element{
    const [newCredentials, setNewCredentials] = useState<newCredentials>({
        "email": undefined,
        "password": undefined,
        "newPassword": undefined})
    const [newPasswordApproval, setNewPasswordApproval] = useState("")

    const handleSubmit = async (event:React.FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        console.log(newCredentials)
        if((newCredentials.newPassword === newPasswordApproval) && ((typeof newCredentials.password || typeof newCredentials.newPassword || typeof newPasswordApproval) !== undefined)){
            await postgresqlDatabase
                .post("/user/changePassword", newCredentials)
                .catch((error:AxiosError) => {
                    console.log(error)
                })
        }
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
            <input type="password" id="newPasswordApproval" name="passwordApproval" onChange={(event) => {setNewPasswordApproval(event.target.value)}}/><br/>
            <input type="submit" value="Potwierdź"/>
        </form>
    )
}