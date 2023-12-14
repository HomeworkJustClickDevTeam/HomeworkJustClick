import { useState } from "react"
import { changePasswordPostgresService } from "../../services/postgresDatabaseServices"
import { AxiosError } from "axios"
import { CredentialsInterface } from "../../types/CredentialsInterface"


export default function UserSecuritySettingsPage(): JSX.Element {
  const [newCredentials, setNewCredentials] = useState<CredentialsInterface>({
    "email": undefined,
    "password": undefined,
    "newPassword": undefined
  })
  const [newPasswordApproval, setNewPasswordApproval] = useState("")

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if ((newCredentials.newPassword === newPasswordApproval) && ((newCredentials.password || newCredentials.newPassword || newPasswordApproval) !== undefined)) {
      await changePasswordPostgresService(newCredentials)
        .catch((error: AxiosError) => {
          console.log(error)
        })
    }
  }
  return (
    <form onSubmit={handleSubmit}>
        <div className='pb-12'>
      <section className='py-1'><label htmlFor="emailVerification">Email: </label>
      <input type="email" id="emailVerification" name="password"
             onChange={
               (event) => {
                 setNewCredentials((prevState) => (
                   {
                     ...prevState,
                     "email": event.target.value
                   }))
               }} className='pl-1 border-b solid black focus:border focus:border-solid'/>
      <br/>
      </section>
            <section className='py-1'>
      <label htmlFor="oldPassword">Stare hasło: </label>
      <input type="password" id="oldPassword" name="password"
             onChange={
               (event) => {
                 setNewCredentials((prevState) => ({
                   ...prevState,
                   "password": event.target.value
                 }))
               }
             } className='pl-1 border-b solid black focus:border focus:border-solid'/>
      <br/>
            </section>
            <section className='py-1'>
      <label htmlFor="newPassword">Nowe hasło: </label>
      <input type="password" id="newPassword" name="password"
             onChange={
               (event) => {
                 setNewCredentials((prevState) => ({
                   ...prevState,
                   "newPassword": event.target.value
                 }))
               }
             } className='pl-1 border-b solid black focus:border focus:border-solid'/>
      <br/>
            </section>
            <section className='py-1'>
      <label htmlFor="newPasswordApproval">Potwierdź nowe hasło: </label>
      <input type="password" id="newPasswordApproval" name="passwordApproval" onChange={(event) => {
        setNewPasswordApproval(event.target.value)
      }} className='pl-1 border-b solid black mb-2 focus:border focus:border-solid'/><br/></section>
        <button type="submit" className='absolute bg-main_blue text-white rounded-md text-sm p-1 active:mt-0.5 active:shadow-md'>Potwierdź</button>
    </div>
      </form>
  )
}