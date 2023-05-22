import React, {ChangeEvent, useState} from "react";
import common_request from "../../services/default-request-database";
import {useNavigate} from "react-router-dom";
import {RegisterUser} from "../../types/types";

const Register = () => {

    const [secondPassword,setSecondPassword] = useState<string>("")
    const [user,setUser] =useState<RegisterUser>({firstname:"",
        lastname: "",
        email:"",
        password:""})
    const navigate = useNavigate()

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        if(!!user.password && user.password === secondPassword){
            try {
                await common_request.post("/auth/register",user)

               navigate("/")
            }catch (e){
                console.log("Error not send")
            }
        }

    }
   const handleChangeUser = (event: ChangeEvent<HTMLInputElement>) => {
        const {name, value} = event.target
       setUser((prevState) => ({
           ...prevState,
           [name]: value,
       }))

    }

    const handleChangeSecondPassword = (event: ChangeEvent<HTMLInputElement>) =>{
        setSecondPassword(event.target.value)
    }

    return(
     <>
         <h1>Dołącz do nas !</h1>
         <form onSubmit={handleSubmit}>
            <input type="text" name="firstname" value={user.firstname} placeholder="Imię" onChange={handleChangeUser}/>
            <input type="text"  name="lastname" value={user.lastname} placeholder="Nazwisko" onChange={handleChangeUser}/>
            <input type="email" name="email" value={user.email} placeholder="Adres e-mail" onChange={handleChangeUser}/>
            <input type="password" name="password" value={user.password} placeholder="Haslo" onChange={handleChangeUser}/>
             <input type="password" name="secondPassword" value={secondPassword} placeholder="Powtorz haslo jeszcze raz" onChange={handleChangeSecondPassword}/>
             <button type="submit">
                Zajerestruj się
             </button>
         </form>
     </>
    );

}
export default Register;