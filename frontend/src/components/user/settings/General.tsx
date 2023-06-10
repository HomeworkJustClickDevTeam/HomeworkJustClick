import userContext from "../../../UserContext";
import Loading from "../../animations/Loading";
import React, {useContext, useEffect, useState} from "react";
import postgresqlDatabase from "../../../services/postgresDatabase";
import {AxiosError} from "axios";

export default function General(){
    const {loggedIn, userState} = useContext(userContext);
    const [loading, setLoading] = useState<boolean>(true)
    const [index, setIndex] = useState<number| undefined>(undefined)


    const handleIndexSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault()
            await postgresqlDatabase
                .put(`/user/index/${userState.userId}`,
                index,
                {
                    headers:{
                        Authorization: `Bearer ${userState.token}`,
                    },
                })
                .catch((error:AxiosError) => {
                    console.log(error)
                })

    }

    useEffect(() => {
        const getIndex =  () => {
            postgresqlDatabase
                .get(`/user/${userState.userId}`)
                .then(response => setIndex(response.data.index))
                .catch(() => setIndex(undefined))
        }
        getIndex()
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
        </>
    )
}