import React, {useContext, useEffect, useState} from "react";
import postgresqlDatabase from "../../../services/postgresDatabase";
import userContext from "../../../UserContext";
import Loading from "../../animations/Loading";
import {AxiosError} from "axios";

export default function Appearance() {
    const [color, setColor] = useState<number | undefined>(undefined)
    const {loggedIn, userState} = useContext(userContext);
    const [loading, setLoading] = useState<boolean>(true)

    const handleColorChange = async (event:React.ChangeEvent<HTMLInputElement>) => {
        setColor(+event.target.value)
        try{
            await postgresqlDatabase
                .put(`/user/color/${userState.userId}`,
                    +event.target.value,
                    {
                        headers:{
                            Authorization: `Bearer ${userState.token}`,
                        },
                    }
                ).catch((error:AxiosError) => {
                    console.log("AXIOS ERROR: ", error)
                })
        }
        catch (e){
            console.log(e);
        }
    }
    useEffect(() => {
        const getColor =  () => {
            postgresqlDatabase
                .get(`/user/${userState.userId}`)
                .then(response => setColor(response.data.color))
                .catch(() => setColor(undefined))
        }
        getColor()
        setLoading(false)
    }, [userState.userId])
    if(loading){
        return <Loading></Loading>
    }
    if (!loggedIn) {
        return <>Not log in</>
    }
    return(
        <>
            <p>Wybierz swój kolor:</p>
            <form>
                {Array.apply(0, Array(20)).map((x, i) => {
                    return (
                        <label key={i}>
                            <input  type="radio" value={i.toString()} name="index" checked={color === i} onChange={handleColorChange}/>
                            {i}
                        </label>
                    )
                })}
            </form>
        </>
    )
}