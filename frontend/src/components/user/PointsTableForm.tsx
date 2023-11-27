import { Button, Table } from "../../types/Table.model"
import { ChangeEvent, FormEvent, useEffect, useState } from "react"
import { createEvaluationPanel } from "../../services/postgresDatabaseServices"
import { Simulate } from "react-dom/test-utils"
import error = Simulate.error
import { useAppSelector } from "../../types/HooksRedux"
import { selectUserState } from "../../redux/userStateSlice"

export default function PointsTableForm(props: {
  setIsFormHidden: (isFormHidden: boolean) => void
  table?: Table
}) {
  const userState = useAppSelector(selectUserState)
  const [table, setTable] = useState<Table>(new Table("", [], 0, userState?.id))
  useEffect(() => {
    if (props.table) {
      setTable(props.table)
      setTable((prevState) => ({
        ...prevState,
        userId: userState?.id,
      }))
    }
    console.log(userState?.id)
  }, [])

  const handleSubmit = (event: FormEvent) => {
    console.log(table)
    event.preventDefault()
    createEvaluationPanel(table)
      .then(() => resetAndHideForm())
      .catch((error) => console.log(error))
  }
  const handleValueChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setTable((prevState) => ({
      ...prevState,
      [name]: value,
    }))
  }

  const handlePointsTableChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    const numbersArray = value
      .split(/[ ,]+/)
      .map((number) => parseFloat(number))
      .filter((number) => !isNaN(number))

    const buttonArray = numbersArray.map((number) => new Button(number))

    setTable((prevState) => ({
      ...prevState,
      [name]: buttonArray,
    }))
  }

  const handleCancel = () => {
    resetAndHideForm()
  }

  const resetAndHideForm = () => {
    setTable(new Table("", [], 0, 0))
    props.setIsFormHidden(true)
  }

  return (
    <div>
      <h1>NOWA TABELA</h1>
      <form onSubmit={handleSubmit}>
        <label>
          Nazwa:
          <input
            name="name"
            type="text"
            onChange={handleValueChange}
            placeholder={"Nazwa tabeli oceniania"}
          />
        </label>
        <label>Punkty: </label>
        <input name="buttons" type="text" onChange={handlePointsTableChange} />
        <label>
          Wiersze w tabeli (max 5):
          <input
            type="number"
            name="width"
            max="5"
            onChange={handleValueChange}
          />
        </label>
        <button
          type="submit"
          className="mr-6 mt-4 px-10 py-1 rounded-lg bg-main_blue text-white hover:bg-hover_blue hover:shadow-md active:shadow-none"
        >
          {" "}
          Zapisz{" "}
        </button>
      </form>
      <button
        onClick={() => handleCancel()}
        className="mr-6 mb-4 px-4 py-1 rounded-lg bg-berry_red text-white"
      >
        {" "}
        X{" "}
      </button>
    </div>
  )
}
