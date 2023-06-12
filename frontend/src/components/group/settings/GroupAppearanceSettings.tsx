import React, {useContext, useEffect, useState} from "react";
import userContext from "../../../UserContext";
import postgresqlDatabase from "../../../services/postgresDatabase";
import {AxiosError} from "axios/index";
import Loading from "../../animations/Loading";
import {useParams} from "react-router-dom";

export default function GroupAppearanceSettings(){
  const [color, setColor] = useState<number | undefined>(undefined)
  const {id} = useParams<{id: string}>()

  const handleColorChange = async (event:React.ChangeEvent<HTMLInputElement>) => {
    setColor(+event.target.value)
    try{
      await postgresqlDatabase
        .put(`/group/color/${id}`, +event.target.value).catch((error:AxiosError) => {
          console.log("AXIOS ERROR: ", error)
        })
    }
    catch (e){
      console.log(e);
    }
  }
  useEffect(() => {
    postgresqlDatabase
        .get(`/group/${id}`)
        .then(response => setColor(response.data.color))
        .catch(() => setColor(undefined))
  }, [id])
  return(

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
  )

}