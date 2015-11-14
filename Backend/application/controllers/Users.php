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
		$id = $this->input->post('id');
		$name = $this->input->post('name');
		$result['status']=0;
		$result['message']="Login Failed";
		$row = $this->db->get_where('users',array('id'=>$id))->row_array();
		if ($row)
		{
			$result['status']=1;
			$result['message']=$row;
		}
		else
		{
			$row['id'] = $id;
			$row['name'] = $name;
			$this->db->insert('users',$row);
			$row = $this->db->get_where('users',array('id'=>$id))->row_array();
			if ($row)
			{
				$result['status']=1;
				$result['message']=$row;
			}
		}
		print json_encode($result);
	}
	public function chaneLevel($id)
	{
		$id = $this->input->post('id');
		$level = $this->input->post('level');
		
	}
}
