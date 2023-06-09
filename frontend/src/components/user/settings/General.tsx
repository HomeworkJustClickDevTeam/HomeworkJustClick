import userContext from "../../../UserContext";
import Loading from "../../animations/Loading";
import React, {useContext, useEffect, useState} from "react";
import postgresqlDatabase from "../../../services/postgresDatabase";

export default function General(){
    const {loggedIn, userState} = useContext(userContext);
    const [loading, setLoading] = useState<boolean>(true)
    const [index, setIndex] = useState<number| undefined>(undefined)
    const [color, setColor] = useState<number | undefined>(undefined)


    const handleIndexSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault()

        try{
            await postgresqlDatabase
                .put(`/user/index/${userState.userId}`,
                index,
                {
                    headers:{
                        Authorization: `Bearer ${userState.token}`,
                    },
                }
            )
        }
        catch (e){
            console.log(e);
        }
    }
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
                )
        }
        catch (e){
            console.log(e);
        }
    }
    useEffect(() => {
        const getIndex = async () => {
            postgresqlDatabase
                .get(`/user/${userState.userId}`)
                .then(response => setIndex(response.data.index))
                .catch(() => setIndex(undefined))
        }
        const getColor = async () => {
            postgresqlDatabase
                .get(`/user/${userState.userId}`)
                .then(response => setColor(response.data.color))
                .catch(() => setColor(undefined))
        }
        getIndex()
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
            <form onSubmit={handleIndexSubmit}>
                <label htmlFor="index">Indeks: </label>
                <input type="number" id="index" name="index" defaultValue={index} onChange={(e) => setIndex(+e.target.value)}/>
                <input type="submit" value="Potwierdź"/>

            </form>
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