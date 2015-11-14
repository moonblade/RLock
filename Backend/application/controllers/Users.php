<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Users extends CI_Controller {

	/**
	 * Index Page for this controller.
	 *
	 * Maps to the following URL
	 * 		http://example.com/index.php/welcome
	 *	- or -
	 * 		http://example.com/index.php/welcome/index
	 *	- or -
	 * Since this controller is set as the default controller in
	 * config/routes.php, it's displayed at http://example.com/
	 *
	 * So any other public methods not prefixed with an underscore will
	 * map to /index.php/welcome/<method_name>
	 * @see http://codeigniter.com/user_guide/general/urls.html
	 */
	
	public function index()
	{
		print('User Functions');
	}

	public function login()
	{
		$email = $this->input->post('email');
		$pass = md5($this->input->post('pass'));
		$result['status']=0;
		$result['message']="Incorrect Email or Password";
		$row = $this->db->get_where('users',array('email'=>$email,'pass'=>$pass))->row_array();
		if ($row)
		{
			$result['status']=1;
			$result['message']=$row;
		}
		print json_encode($result);
	}

	public function signup()
	{
		$result['status']=0;
		$result['message']="Signup Failed";
		$newUser['email'] = $this->input->post('email');
		$newUser['name'] = $this->input->post('name');
		$newUser['pass'] = md5($this->input->post('pass'));
		if($this->db->get_where('users',array('email'=>$newUser['email']))->row_array())
		{
			$result['status']=0;
			$result['message']="Email already in use";
		}
		else if($this->db->insert('users',$newUser))
		{
			$result['status']=1;
			$result['message']="Signup Successful";	
		}
		print json_encode($result);
	}

	public function changePass()
	{
		$result['status']=0;
		$result['message']="Change Password Failed";
		$oldUser['id']=$this->input->post('id');
		$oldUser['pass']=md5($this->input->post('pass'));
		$newUser['pass']=md5($this->input->post('newpass'));
		$this->db->update('users',$newUser,$oldUser);
		if($this->db->affected_rows())
		{
			$result['status']=1;
			$result['message']="Password Change Successful";	
		}
		print json_encode($result);
	}
}
