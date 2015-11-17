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
	public function changeLevel($id=0)
	{
		$temp['id']=$id;
		$result['status']=0;
		$result['message']="Insufficient Permissions";
		$toSearch['id'] = $this->input->post('id');
		$toChange['level'] = $this->input->post('level');
		$changerLevel = $this->db->get_where('users',$temp)->row_array()['level'];
		$changeeLevel = $this->db->get_where('users',$toSearch)->row_array()['level'];
		if($changerLevel>1)
		{
			$result['message']="Database Error";

			if($changerLevel<$changeeLevel || $toChange['level']>$changerLevel || $toChange['level']<0)
			{
				$result['message']=$this->getResponse(rand(0,200));
			}
			else
			{
				$this->db->update('users',$toChange,$toSearch);
				if($this->db->affected_rows())
				{
					$result['status']=1;
					$result['message']="Access Changed";
					if($toSearch['id']==$temp['id'])
					{
						$result['message']="You know you just demoted yourself, right?";
					}
				}	
			}
		}
		print json_encode($result);
	}

	public function viewUsers($id=0)
	{
		$result['status']=0;
		$result['message']="Insufficient Permissions";
		$toSearch['id']=$id;
		if($this->db->get_where('users',$toSearch)->row_array()['level']>1)
		{
			$result['status']=1;
			$this->db->order_by('level','DESC');
			$this->db->order_by('name');
			$query=$this->db->get('users');
			$result['message']=$query->result();
		}
		print json_encode($result);
	}

	private function getResponse($value=0)
	{
		$nor=15;
		switch($value%$nor)
		{
			case 0:
				return "You really thought you could do that?";
			case 1:
				return "Someone ought to teach you some manners";
			case 3:
				return "Funny you should try that";
			case 4:
				return "Ha Ha Ha, I laugh at you";
			case 5:
				return "Why don't you take on someone your own size";
			case 6:
				return "Go away";
			case 7:
				return "Why are you doing this";
			case 8:
				return "Stop it!";
			case 9:
				return "I don't like you";
			case 10:
				return "Is this going to be a thing with you?";
			case 11:
				return "I can't belive you tried it again";
			case 12:
				return "THIS IS SPARTA!!!!";
			case 13:
				return "Running out of funny... must recharge";
			default:
				return "You really thought you could do that?";
		}
	}
}
