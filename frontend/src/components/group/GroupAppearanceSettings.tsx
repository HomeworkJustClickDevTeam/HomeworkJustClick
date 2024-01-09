import React from "react"
import { changeGroupColorPostgresService } from "../../services/postgresDatabaseServices"
import { AxiosError } from "axios"
import { colorsArray } from "../../assets/colors"
import { selectGroup, setGroup } from "../../redux/groupSlice"
import { AppDispatch } from "../../redux/store"
import { useAppDispatch, useAppSelector } from "../../types/HooksRedux"

export default function GroupAppearanceSettings() {
  const dispatch:AppDispatch = useAppDispatch()
  const group= useAppSelector(selectGroup)
  const handleColorChange = (event: React.ChangeEvent<HTMLInputElement>) => {
      if(group !== null){
        let _group = {...group}
        _group.color = +event.target.value
        changeGroupColorPostgresService(group?.id as unknown as string, +event.target.value)
          .catch((error: AxiosError) => {
          console.log("AXIOS ERROR: ", error)
        })
          .then(()=>dispatch(setGroup(_group)))
      }
  }
  return (

    <form className='flex inline-block flex-wrap gap-2'>
      {Array.apply(0, Array(20)).map((x, i) => {
        return (
          <div key={i} className='flex inline-block flex-wrap w-fit'>
            <label className='flex inline-block pr-1'>
              <input type="radio" value={i.toString()} name="index" checked={group?.color === i} onChange={handleColorChange}
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