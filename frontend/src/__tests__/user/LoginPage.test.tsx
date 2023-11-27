import renderer from "react-test-renderer";
import { Provider } from "react-redux";
import { setupStore } from "../../redux/store";
import LoginPage from "../../components/user/LoginPage";
import { loginPageHandler } from "../../mocks/handlers";
import { renderWithProviders } from "../../utils/test-utils";
import { fireEvent, screen } from "@testing-library/react";
import { BrowserRouter, MemoryRouter, Route, Routes } from "react-router-dom";
import userEvent from "@testing-library/user-event";
import { setupServer } from "msw/node";
import '@testing-library/jest-dom';
import React from "react";
import App from "../../App";
const server = setupServer(...loginPageHandler)
beforeAll(() => server.listen())
beforeEach(()=> {
  jest.clearAllMocks()
})
afterEach(()=> server.resetHandlers())
afterAll(() => server.close())


describe("check if user can login with page navigation", ()=> {
  it("checks if login page renders correctly", () => {
    const renderedHomeGuestPage = renderer
      .create(
        <Provider store={setupStore()}>
          <BrowserRouter>
            <LoginPage></LoginPage>
          </BrowserRouter>
        </Provider>
      )
    expect(renderedHomeGuestPage).toMatchSnapshot()
  });

  it("checks if user can login", async ()=>{
    const user = userEvent.setup()
    renderWithProviders(
      <MemoryRouter initialEntries ={['/login']}>
        <App/>
      </MemoryRouter>
    )
    const registerButton = screen.getByRole('button', {name: /zarejestruj się/i})
    const loginButton = screen.getByRole('button', {name: /zaloguj się/i})
    const emailTextBox = screen.getByPlaceholderText(/Email/i)
    const passwordTextBox = screen.getByPlaceholderText(/hasło/i)
    expect(registerButton).toBeEnabled()
    expect(loginButton).toBeEnabled()
    expect(screen.getByRole('heading',{name: /zaloguj się/i})).toBeInTheDocument()
    expect(emailTextBox).toBeInTheDocument()
    expect(passwordTextBox).toBeInTheDocument()
    expect(passwordTextBox).toBeEmptyDOMElement()
    expect(emailTextBox).toBeEmptyDOMElement()
    await user.type(emailTextBox, "adam_nowak@gmail.com")
    expect(emailTextBox).toHaveValue("adam_nowak@gmail.com")
    await user.type(passwordTextBox, "123")
    expect(passwordTextBox).not.toHaveValue("")
    await user.click(loginButton)
    const pageNavigation = await screen.findAllByText(/moje grupy/i)
    expect(pageNavigation).toBeInstanceOf(Array)
    expect(pageNavigation).toHaveLength(2)
  })

})

