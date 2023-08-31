import React, {useEffect, useState} from "react";
import {getGroupPostgresService, putGroupColorPostgresService} from "../../services/postgresDatabaseServices";
import {AxiosError} from "axios";
import {useParams} from "react-router-dom";
import {colorsArray} from "../../assets/colors";

export default function GroupAppearanceSettings() {
  const [color, setColor] = useState<number | undefined>(undefined)
  const {idGroup} = useParams<{ idGroup: string }>()

  const handleColorChange = async (event: React.ChangeEvent<HTMLInputElement>) => {
    setColor(+event.target.value)
    try {
      await putGroupColorPostgresService(idGroup as string, +event.target.value).catch((error: AxiosError) => {
        console.log("AXIOS ERROR: ", error)
      })
    } catch (e) {
      console.log(e);
    }
    console.log(color)
  }
  useEffect(() => {
    getGroupPostgresService(idGroup as string)
      .then(response => setColor(response.data.color))
      .catch(() => setColor(undefined))
  }, [idGroup])
  return (

    <form className='flex inline-block flex-wrap gap-2'>
      {Array.apply(0, Array(20)).map((x, i) => {
        return (
          <div className='flex inline-block flex-wrap w-fit'>
            <label key={i} className='flex inline-block pr-1'>
              <input type="radio" value={i.toString()} name="index" checked={color === i} onChange={handleColorChange}
                     className='w-fit cursor-pointer'/>
              <div
                className={`h-5 w-5 ${colorsArray[i]} border border-solid border-black items-center mt-0.5 ml-1`}></div>
            </label>
          </div>
        )
      })}
    </form>
  )

}