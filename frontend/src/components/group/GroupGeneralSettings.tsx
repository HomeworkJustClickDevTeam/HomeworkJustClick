import { useNavigate } from "react-router-dom"
import {
  archiveGroupPostgresService,
  changeGroupDescriptionPostgresService,
  changeGroupNamePostgresService,
  deleteGroupPostgresService,
  unarchiveGroupPostgresService
} from "../../services/postgresDatabaseServices"
import { AxiosError } from "axios"
import React from "react"
import { selectGroup, setGroup } from "../../redux/groupSlice"
import { GroupInterface } from "../../types/GroupInterface"
import { useAppDispatch, useAppSelector } from "../../types/HooksRedux"
import {toast} from "react-toastify";

export default function GroupGeneralSettings() {
  const navigate = useNavigate()
  const group= useAppSelector(selectGroup)
  const currentURL = `${window.location.origin}/group/${group?.id}`
  const dispatch = useAppDispatch()
  const [updatedGroup, setUpdatedGroup] = React.useState<GroupInterface>(group!)

  const groupDeletionHandler = () => {
    deleteGroupPostgresService(group?.id as unknown as string)
      .catch((error: AxiosError) => console.log(error))
    dispatch(setGroup(null))
    navigate("/")
  }
  const archivizationHandler = () => {
    group?.archived
      ? unarchiveGroupPostgresService(group?.id as unknown as string)
        .catch((error: AxiosError) => console.log(error))
        .then(() =>{
          let _group = group
          if(_group !== undefined){
            _group.archived = !_group?.archived
            dispatch(setGroup(_group))
          }
        })
      : archiveGroupPostgresService(group?.id as unknown as string)
        .catch((error: AxiosError) => console.log(error))
        .then(() =>{
          let _group = {...group}
          if(_group !== null){
            _group.archived = !_group?.archived as boolean
            dispatch(setGroup(_group as GroupInterface))
          }
        })
  }

  const setGroupName = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if(group !== null) {
      changeGroupNamePostgresService(group?.id as unknown as string, updatedGroup.name)
        .then(()=>{
          toast.success("Udało się zmienić nazwę grupy.")
          dispatch(setGroup(updatedGroup))
        })
        .catch((error: AxiosError) => {
          toast.error("Coś poszło nie tak.")
          console.log(error)
        })
    }
  }
  const setGroupDescription = (
    event: React.FormEvent<HTMLFormElement>
  ) => {
    event.preventDefault()
    if(group !== null) {
      changeGroupDescriptionPostgresService(group.id as unknown as string, updatedGroup.description)
        .then(()=>{
          toast.success("Udało się zmienić opis grupy.")
          dispatch(setGroup(updatedGroup))
        })
        .catch((error: AxiosError) => {
          toast.error("Coś poszło nie tak.")
          console.log(error)
        })
    }
  }
  const handleCopyUrl = async () => {
    try {
      await navigator.clipboard.writeText(currentURL)
      toast.success('Link został skopiowany!')
    } catch (error) {
      console.log(error)
    }
  }
  return (
    <>
      <ul>
        <li>
          <form onSubmit={setGroupName}>
            <label htmlFor="groupName">
              Nazwa Grupy:
              <input
                maxLength={65}
                type="text"
                required={true}
                name="groupName"
                defaultValue={group?.name}
                onChange={(event) =>{
                  let _group = {...group}
                  if(_group !== null){
                    _group.name = event.target.value
                    setUpdatedGroup(_group as GroupInterface)
                  }}
                }
                className='border-b-2 border-b-light_gray mx-2 mr-8 focus:outline-none focus:border-b-main_blue'
              />
              <button type="submit" className='bg-main_blue px-4 text-white rounded-md'>Potwierdź</button>
            </label>
          </form>
        </li>
        <li>
          <form onSubmit={setGroupDescription}>
            <label htmlFor="groupDescription">
              Opis Grupy:
              <input
                maxLength={65}
                required={true}
                type="text"
                name="groupDescription"
                defaultValue={group?.description}
                onChange={(event) =>{
                    let _group = {...group}
                    if(_group !== null){
                      _group.description = event.target.value
                      setUpdatedGroup(_group as GroupInterface)
                    }
                }
               }
                className='border-b-2 border-b-light_gray mx-2 mr-8 focus:outline-none focus:border-b-main_blue'
              />
              <button className='bg-main_blue px-4 text-white rounded-md'>Potwierdź</button>
            </label>
          </form>
        </li>
        <li>
          <label htmlFor="archived" className="switch">
            Zarchiwizowana:
            <input
              className='ml-2 cursor-pointer'
              name="archived"
              type="checkbox"
              defaultChecked={group?.archived}
              onClick={archivizationHandler}
            />
            <span className="slider"></span>
          </label>
        </li>
        <li>
          <button onClick={groupDeletionHandler} className='text-scarlet '>Usuń grupę</button>
        </li>
        <li>
          <button onClick={handleCopyUrl} className='underline'>Udostepnij Link grupy</button>
        </li>
      </ul>
    </>
  )
}
