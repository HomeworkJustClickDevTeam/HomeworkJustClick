import ReactDatePicker from "react-datepicker";
import {AssignmentModifyFile} from "./AssignmentModifyFile";
import React, {ChangeEvent, SetStateAction, useEffect, useState} from "react";
import {AssignmentInterface} from "../../types/AssignmentInterface";
import {Table} from "../../types/Table.model";
import {AssignmentToSendInterface} from "../../types/AssignmentToSendInterface";
import {GroupInterface} from "../../types/GroupInterface";
import {useNavigate} from "react-router-dom";
import {AssignmentAddFile} from "./AssignmentAddFile";
import {CommentInterface} from "../../types/CommentInterface";

interface AssignmentSettingsPagePropsInterface{
  handleSubmit: (event: React.FormEvent) => void,
  assignment: AssignmentInterface|AssignmentToSendInterface,
  chosenEvaluationTable:number,
  toSend: boolean,
  handleDelete?:(event: React.FormEvent) => void,
  setChosenEvaluationTable:(tableId: number)=>void,
  evaluationTable:Table[],
  setAssignment: (assignment: (prevState: any) => any) => void,
  group: GroupInterface|null,
  newAssignmentId?: number
}

export const AssignmentSettingsPage = ({handleSubmit,
                                         assignment,
                                         chosenEvaluationTable,
                                         handleDelete,
                                         toSend,
                                         setChosenEvaluationTable,
                                         evaluationTable,
                                         setAssignment,
                                         group,
                                         newAssignmentId}:AssignmentSettingsPagePropsInterface) =>{

  const [toNavigate, setToNavigate] = useState<boolean>(false)
  const navigate = useNavigate()
  const handleTextChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setAssignment((prevState) => ({
      ...prevState,
      [name]: value,
    }))
  }
  const handleNumberChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setAssignment((prevState) => ({
      ...prevState,
      [name]: value,
    }))
  }

  const handleCheckboxChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, checked } = event.target
    setAssignment((prevState) => ({
      ...prevState,
      [name]: checked,
    }))
  }

  const handleDateChange = (date: Date) => {
    setAssignment((prevState) => ({
      ...prevState,
      completionDatetime: date,
    }))
  }

  useEffect(() => {
    if (toNavigate) {
      navigate(`/group/${group?.id}/assignments/`)
    }
  }, [toNavigate])

  const handleTableChange = (event:React.ChangeEvent<HTMLSelectElement>) => {
    if(+event.target.value === -1)
      setChosenEvaluationTable(-1)
    else
    {
      setChosenEvaluationTable(+event.target.value)
      const chosenTable = evaluationTable.filter(table => table.id === +event.target.value)[0]
      setAssignment(prevState => (
        {...assignment,
          max_points: Math.max(...chosenTable.buttons.map(button => button.points))})
      )
    }

  }
  const evaluationTables = ():JSX.Element[] => {
    const options = evaluationTable.map(table =>
      <option value={table.id} key={table.id}>{table.name}</option>
    )
    options.push(<option value={-1} key={-1}>Brak tabeli</option>)
    return options
  }
  return (
    <>
      <div>
        <div className="relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 fit pb-4">
          <form onSubmit={(event) => handleSubmit(event)} className="flex flex-col gap-3">
            <label className="pr-3">
              Tytuł
              <input
                name="title"
                type="text"
                onChange={(event)=>handleTextChange(event)}
                value={assignment.title}
                placeholder="Nazwa zadania"
                className="pl-1 ml-2 border-b-2 border-b-light_gray w-64"
              />
            </label>

            <label>
              Opis zadania
              <input
                name="taskDescription"
                type="text"
                onChange={(event)=>handleTextChange(event)}
                value={assignment.taskDescription}
                placeholder="Opis zadania"
                className="pl-1 ml-2 border-b-2 border-b-light_gray w-80"
              />
            </label>
            <label>
              Maksymalne punkty
              <input
                disabled={chosenEvaluationTable !== -1}
                name="max_points"
                type="number"
                onChange={(event)=>handleNumberChange(event)}
                min="0"
                value={assignment.max_points}
                className="pl-1 ml-2 border-b-2 border-b-light_gray cursor-pointer w-12"
              />
            </label>
            <label>Wybierz tabelę:
              <select className="ml-2 pr-1 border-2 border-border_gray border-solid rounded-md" onChange={(event) => handleTableChange(event)} value={chosenEvaluationTable}>
                {evaluationTables()}
              </select>
            </label>
            <label>
              {" "}
              Kara za wysłanie po terminie (%)
              <input
                name="auto_penalty"
                type="number"
                onChange={handleNumberChange}
                min="0"
                max="100"
                step="25"
                value={assignment.auto_penalty}
                className="pl-1 ml-2 border-b-2 border-b-light_gray cursor-pointer w-12"
              />
            </label>
            <label className="flex">
              <p className="w-36">Data wykonania: </p>
              <ReactDatePicker
                name="completionDatetime"
                selected={assignment.completionDatetime}
                onChange={handleDateChange}
                showTimeSelect
                timeFormat="HH:mm"
                timeIntervals={15}
                dateFormat="yyyy-MM-dd HH:mm"
                className="pl-1 ml-2 border-b-2 border-b-light_gray w-36 cursor-pointer"
              />
            </label>
            <label>
              Widoczne:
              <input
                name="visible"
                type="checkbox"
                checked={assignment.visible}
                onChange={(event)=>handleCheckboxChange(event)}
              />
            </label>
            <button
              type={"submit"}
              className="absolute top-0 right-0 mr-6 mt-4 px-10 py-1 rounded-lg bg-main_blue text-white hover:bg-hover_blue hover:shadow-md active:shadow-none"
            >
              Zapisz
            </button>
          </form>
          <p className="mt-4 mb-2">Dodaj pliki: </p>
          {'id' in assignment && newAssignmentId === undefined ?
          <AssignmentModifyFile
            toSend={toSend}
            assignmentId={assignment.id}
            setToNavigate={setToNavigate}
          />:
            <AssignmentAddFile
              toSend={toSend}
              idAssignment={newAssignmentId}
              setToNavigate={setToNavigate}
            />}
          {handleDelete !== undefined &&
          <button onClick={(event) => handleDelete(event)}
                  className='absolute bottom-0 right-0 mr-6 mb-4 px-4 py-1 rounded-lg bg-berry_red text-white'>Usuń Zadanie
          </button>}
        </div>
      </div>
    </>
  )
}