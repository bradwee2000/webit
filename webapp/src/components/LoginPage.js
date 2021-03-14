import { LoginButton } from './common/Commons';

function LoginPage({onLoginSuccess}) {

  return (
    <>
      <LoginButton onLoginSuccess={onLoginSuccess}/>
    </>
  );
}

export default LoginPage;
