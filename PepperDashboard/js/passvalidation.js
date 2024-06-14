$('#psw, #confirm_psw').on('keyup', function () {
  if ($('#psw').val() == $('#confirm_psw').val()) {
  	$('#confirm_psw').removeClass('is-invalid');
    $('#confirm_psw').addClass('is-valid');
  } else
  {
  	$('#confirm_psw').removeClass('is-valid');
    $('#confirm_psw').addClass('is-invalid');
  }
  if ($('#psw').val() == $('#confirm_psw').val() && $('#confirm_psw').val() == '') {
  	$('#confirm_psw').removeClass('is-invalid');
    $('#confirm_psw').removeClass('is-valid');
  } 
});
